package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class LiteralDict extends Node {

    public List<Node> keys;
    public List<Node> values;

    public LiteralDict(Token token, List<Node> keys, List<Node> values) {
        super(token);
        this.keys = keys;
        this.values = values;
    }

}
