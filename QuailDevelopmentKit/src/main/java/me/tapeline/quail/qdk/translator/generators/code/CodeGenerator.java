package me.tapeline.quail.qdk.translator.generators.code;

import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import org.jetbrains.annotations.Nullable;

public abstract class CodeGenerator {

    public final Token token;

    public CodeGenerator(Token token) {
        this.token = token;
    }

    public abstract QSourceGenerator generateSource(@Nullable CodeGenerator context);

}
