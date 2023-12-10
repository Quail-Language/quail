package me.tapeline.quail.qdk.translator.generators.code.variable;

import me.tapeline.quail.qdk.translator.GenUtils;
import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.source.QSimpleSource;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class VariableGenerator extends CodeGenerator {

    private final String variable;
    private final Integer[] modifiers;

    public VariableGenerator(Token token, String variable, int[] modifiers) {
        super(token);
        this.variable = variable;
        this.modifiers = Arrays.stream(modifiers).boxed().toArray(Integer[]::new);
    }

    @Override
    public QSourceGenerator generateSource(@Nullable CodeGenerator context) {
        return new QSimpleSource(token,
                "scope.get(\"" + variable + "\", new int[] " + GenUtils.arrayToCode(modifiers) + ");"
        );
    }

    public String getVariable() {
        return variable;
    }

    public Integer[] getModifiers() {
        return modifiers;
    }

}
