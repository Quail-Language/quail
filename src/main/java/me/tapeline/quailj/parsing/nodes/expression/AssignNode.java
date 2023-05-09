package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.Locale;

public class AssignNode extends Node {

    public Node variable;
    public Node value;

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
