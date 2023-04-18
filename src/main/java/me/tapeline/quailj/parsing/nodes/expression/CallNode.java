package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.HashMap;
import java.util.List;

public class CallNode extends Node {

    public Node callee;
    public List<Node> args;
    public HashMap<String, Node> kwargs;

    public CallNode(Token token, Node callee, List<Node> args, HashMap<String, Node> kwargs) {
        super(token);
        this.callee = callee;
        this.args = args;
        this.kwargs = kwargs;
    }

}
