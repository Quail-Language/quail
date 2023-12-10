package me.tapeline.quail.qdk.translator.generators.code.literals;

import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.source.QSimpleSource;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import org.jetbrains.annotations.Nullable;

public class NumGenerator extends CodeGenerator {

    private final double number;

    public NumGenerator(Token token, double number) {
        super(token);
        this.number = number;
    }

    @Override
    public QSourceGenerator generateSource(@Nullable CodeGenerator context) {
        return new QSimpleSource(token, 
                new String[] {
                        "me.tapeline.quailj.typing.classes.QObject"
                },
                "QObject.Val(" + number + ")"
        );
    }

}
