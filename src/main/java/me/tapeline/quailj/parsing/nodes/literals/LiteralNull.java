package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class LiteralNull extends Node {

    public LiteralNull(Token token) {
        super(token);
    }

}
