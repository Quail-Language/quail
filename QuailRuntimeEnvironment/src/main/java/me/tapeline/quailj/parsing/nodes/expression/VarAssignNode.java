package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

public class VarAssignNode extends Node {

    public VariableNode variable;
    public Node value;

    public VarAssignNode(Token token, VariableNode variable, Node value) {
        super(token);
        this.variable = variable;
        this.value = value;
    }

    public static VarAssignNode fromAssignNode(AssignNode node) {
        return new VarAssignNode(node.getToken(), ((VariableNode) node.variable), node.value);
    }

    @Override
    public String stringRepr() {
        return "assignvar{" + variable.stringRepr() + " " + value.stringRepr() + "}";
    }

}
