package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class IndexSetNode extends Node {

    public Node collection;
    public Node index;
    public Node value;

    public IndexSetNode(Token token, Node collection, Node index, Node value) {
        super(token);
        this.collection = collection;
        this.index = index;
        this.value = value;
    }

}
