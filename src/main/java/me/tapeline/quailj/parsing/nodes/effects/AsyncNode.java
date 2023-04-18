package me.tapeline.quailj.parsing.nodes.effects;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class AsyncNode extends Node {

    public Node expression;

    public AsyncNode(Token token, Node expression) {
        super(token);
        this.expression = expression;
    }

}
