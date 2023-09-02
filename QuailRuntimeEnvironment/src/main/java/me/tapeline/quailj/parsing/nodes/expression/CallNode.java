package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.HashMap;
import java.util.List;

public class CallNode extends Node {

    public Node callee;
    public List<Node> args;
    public HashMap<String, Node> kwargs;
    public boolean isFieldCall;
    public Node parentField;
    public String field;

    public CallNode(Token token, Node callee, List<Node> args, HashMap<String, Node> kwargs) {
        super(token);
        this.callee = callee;
        this.args = args;
        this.kwargs = kwargs;
        if (callee instanceof FieldReferenceNode) {
            field = ((FieldReferenceNode) callee).value;
            parentField = ((FieldReferenceNode) callee).parent;
            isFieldCall = true;
        }
    }

    @Override
    public String stringRepr() {
        return "call{" + callee.stringRepr() + " " + TextUtils.nodeListToStringRepr(args) + "}";
    }

}
