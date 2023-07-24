package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public abstract class DocNode extends Node {

    public DocNode(Token token) {
        super(token);
    }

}
