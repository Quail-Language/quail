package me.tapeline.quailj.parsing.nodes.generators;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class ListForGeneratorNode extends Node {

    public final Node value;
    public final List<String> iterators;
    public final Node iterable;
    public final @Nullable Node condition;

    public ListForGeneratorNode(Token token, Node value,
                                List<String> iterators,
                                Node iterable,
                                @Nullable Node condition) {
        super(token);
        this.value = value;
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
    }

    @Override
    public String stringRepr() {
        return "genlist{" + value.stringRepr() + " " +
                iterators + " " +
                iterable.stringRepr() + " " +
                (condition != null? condition.stringRepr() : "null") + "}";
    }

}
