package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class LiteralFunction extends Node {

    public String name;
    public List<Node> args;
    public Node code;

    public LiteralFunction(Token token, String name, List<Node> args, Node code) {
        super(token);
        this.name = name;
        this.args = args;
        this.code = code;
    }

}
