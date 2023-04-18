package me.tapeline.quailj.parsing.nodes.expression;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class SubscriptNode extends Node {

    public Node collection;
    public @Nullable Node start;
    public @Nullable Node end;
    public @Nullable Node step;

    public SubscriptNode(Token token, Node collection, @Nullable Node start,
                         @Nullable Node end, @Nullable Node step) {
        super(token);
        this.collection = collection;
        this.start = start;
        this.end = end;
        this.step = step;
    }

}
