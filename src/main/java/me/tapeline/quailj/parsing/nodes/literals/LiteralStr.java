package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

public class LiteralStr extends Node {

    public String value;

    public LiteralStr(Token token) {
        super(token);
        this.value = token.getLexeme();
    }

    public LiteralStr(Token token, String value) {
        super(token);
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return "\"" + value + "\"";
    }

}
