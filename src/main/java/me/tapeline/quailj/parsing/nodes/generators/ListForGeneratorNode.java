package me.tapeline.quailj.parsing.nodes.generators;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class ListForGeneratorNode extends Node {

    public Node value;
    public List<String> iterators;
    public Node iterable;
    public @Nullable Node condition;

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
