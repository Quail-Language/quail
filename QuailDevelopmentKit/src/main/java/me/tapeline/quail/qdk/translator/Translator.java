package me.tapeline.quail.qdk.translator;

import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.code.NodeVisitor;
import me.tapeline.quail.qdk.translator.generators.code.UnsupportedNodeException;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import org.apache.commons.lang3.StringUtils;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Translator {

    public ClassSourceGenerator translateFile(String filePackage, String name, String contents)
            throws LexerException, ParserException, UnsupportedNodeException, ClassNotFoundException {
        Lexer lexer = new Lexer(contents);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(contents, tokens);
        Node node = parser.parse();
        NodeVisitor visitor = new NodeVisitor();
        CodeGenerator rootGenerator = visitor.visit(node);
        QSourceGenerator rootSource = rootGenerator.generateSource(null);

        Set<String> imports = new HashSet<>(Arrays.asList(rootSource.getImports()));
        Class<?>[] classesImported = new Class[imports.size()];
        int ptr = 0;
        for (String className : imports)
            classesImported[ptr++] = Class.forName(className);

        FunctionSourceGenerator main = FunctionSourceGenerator.create("main")
                .useType(classesImported)
                .addBodyCode(StringUtils.join(rootSource.getLines(), "\n"))
                .addModifier(Modifier.PUBLIC)
                .addParameter(VariableSourceGenerator.create(Runtime.class, "runtime"))
                .addParameter(VariableSourceGenerator.create(Memory.class, "scope"))
                .setReturnType(void.class)
                .addThrowable(TypeDeclarationSourceGenerator.create(RuntimeStriker.class));

        ClassSourceGenerator classSourceGenerator = ClassSourceGenerator
                .create(TypeDeclarationSourceGenerator.create(
                        name
                ))
                .addModifier(Modifier.PUBLIC)
                .addMethod(main);

        return classSourceGenerator;
    }

}
