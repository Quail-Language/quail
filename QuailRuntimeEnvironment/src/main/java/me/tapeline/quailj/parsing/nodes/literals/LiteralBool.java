package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class LiteralBool extends Node {

    public final boolean value;

    public LiteralBool(Token token, boolean value) {
        super(token);
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return Boolean.toString(value);
    }

}
