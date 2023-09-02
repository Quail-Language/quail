package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class FieldSetNode extends Node {

    public Node parent;
    public String field;
    public Node value;

    public FieldSetNode(Token token, Node parent, String field, Node value) {
        super(token);
        this.parent = parent;
        this.field = field;
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return "field set{" + parent.stringRepr() + " " + field + " " + value.stringRepr() + "}";
    }
    
}
