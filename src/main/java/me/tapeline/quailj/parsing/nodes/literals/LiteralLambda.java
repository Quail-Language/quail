package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class LiteralLambda extends Node {

    public List<Node> args;
    public Node statement;

    public LiteralLambda(Token token, List<Node> args, Node statement) {
        super(token);
        this.args = args;
        this.statement = statement;
    }

    @Override
    public String stringRepr() {
        return "lambda{" + TextUtils.nodeListToStringRepr(args) + " " + statement.toString() + "}";
    }

}
