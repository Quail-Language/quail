package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class IndexingNode extends Node {

    public Node collection;
    public Node index;

    public IndexingNode(Token token, Node collection, Node index) {
        super(token);
        this.collection = collection;
        this.index = index;
    }

}
