package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

public class LiteralNull extends Node {

    public LiteralNull(Token token) {
        super(token);
    }

    @Override
    public String stringRepr() {
        return "null";
    }

}
