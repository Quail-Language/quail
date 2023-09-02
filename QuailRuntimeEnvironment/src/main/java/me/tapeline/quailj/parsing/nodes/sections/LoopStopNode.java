package me.tapeline.quailj.parsing.nodes.sections;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class LoopStopNode extends Node {

    public Node condition;
    public Node code;

    public LoopStopNode(Token token, Node condition, Node code) {
        super(token);
        this.condition = condition;
        this.code = code;
    }

    @Override
    public String stringRepr() {
        return "loop{" + condition.stringRepr() + " " + code.stringRepr() + "}";
    }

}
