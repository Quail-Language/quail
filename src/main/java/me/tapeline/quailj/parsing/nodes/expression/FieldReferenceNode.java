package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class FieldReferenceNode extends Node {

    public Node parent;
    public String value;

    public FieldReferenceNode(Token token, Node parent, String value) {
        super(token);
        this.parent = parent;
        this.value = value;
    }

}
