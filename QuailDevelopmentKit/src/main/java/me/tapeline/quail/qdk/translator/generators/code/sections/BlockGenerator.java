package me.tapeline.quail.qdk.translator.generators.code.sections;

import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.source.QSimpleSource;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockGenerator extends CodeGenerator {

    private final List<CodeGenerator> generators;

    public BlockGenerator(Token token, List<CodeGenerator> generators) {
        super(token);
        this.generators = generators;
    }

    @Override
    public QSourceGenerator generateSource(@Nullable CodeGenerator context) {
        List<String> imports = new ArrayList<>();
        List<String> code = new ArrayList<>();
        for (CodeGenerator generator : generators) {
            QSourceGenerator generated = generator.generateSource(this);
            imports.addAll(Arrays.asList(generated.getImports()));
            code.add("runtime.setCurrentPosition(" + token.getCharacter() +
                    ", " + token.getLine() + ", " + token.getLength() + ");");
            code.addAll(Arrays.asList(generated.getLines()));
            code.add("");
            code.add("");
        }
        return new QSimpleSource(token, imports.toArray(new String[0]), code.toArray(new String[0]));
    }

}
