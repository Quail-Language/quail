package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class FieldReferenceNode extends Node {

    public final Node parent;
    public final String value;

    public FieldReferenceNode(Token token, Node parent, String value) {
        super(token);
        this.parent = parent;
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return "field{" + parent.stringRepr() + " " + value + "}";
    }

}
