package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class AssignNode extends Node {

    public final Node variable;
    public final Node value;

    public AssignNode(Token token, Node variable, Node value) {
        super(token);
        this.variable = variable;
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return "assign{" + variable.stringRepr() + " " + value.stringRepr() + "}";
    }

}
