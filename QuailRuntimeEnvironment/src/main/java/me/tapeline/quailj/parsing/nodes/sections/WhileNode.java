package me.tapeline.quailj.parsing.nodes.sections;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class WhileNode extends Node {

    public final Node condition;
    public final Node code;

    public WhileNode(Token token, Node condition, Node code) {
        super(token);
        this.condition = condition;
        this.code = code;
    }

    @Override
    public String stringRepr() {
        return "while{" + condition.stringRepr() + " " + code.stringRepr() + "}";
    }

}
