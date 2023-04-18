package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class LiteralNum extends Node {

    public double value;

    public LiteralNum(Token token) {
        super(token);
        this.value = Double.parseDouble(token.getLexeme());
    }

}
