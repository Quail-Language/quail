package me.tapeline.quail.qdk.translator.generators.code.expression;

import me.tapeline.quail.qdk.translator.GenUtils;
import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.code.variable.VariableGenerator;
import me.tapeline.quail.qdk.translator.generators.source.QSimpleSource;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class AssignGenerator extends CodeGenerator {

    private final VariableGenerator variable;
    private final CodeGenerator value;

    public AssignGenerator(Token token, VariableGenerator variable, CodeGenerator value) {
        super(token);
        this.variable = variable;
        this.value = value;
    }

    @Override
    public QSourceGenerator generateSource(@Nullable CodeGenerator context) {
        QSourceGenerator valueSource = value.generateSource(this);
        String[] imports = ArrayUtils.addAll(new String[0], valueSource.getImports());
        return new QSimpleSource(token, imports,
                "if (scope.contains(\"" + variable.getVariable() + "\"))",
                "    scope.set(runtime, \"" + variable.getVariable() + "\", " +
                        StringUtils.join(valueSource.getLines(), "\n") + ");",
                "else",
                "    scope.set(\"" + variable.getVariable() + "\", " +
                        StringUtils.join(valueSource.getLines(), "\n") + ", new int[] " +
                        GenUtils.arrayToCode(variable.getModifiers()) + ");"
        );
    }

}
