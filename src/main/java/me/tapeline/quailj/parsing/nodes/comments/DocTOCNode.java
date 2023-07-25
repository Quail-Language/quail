package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public abstract class DocTOCNode extends Node {
    public DocTOCNode(Token token) {
        super(token);
    }
}
