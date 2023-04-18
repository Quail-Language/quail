package me.tapeline.quailj.parsing.nodes.generators;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

import java.util.List;

public class ListForGeneratorNode extends Node {

    public Node value;
    public List<VariableNode> iterators;
    public Node iterable;
    public @Nullable Node condition;

    public ListForGeneratorNode(Token token, Node value,
                                List<VariableNode> iterators,
                                Node iterable,
                                @Nullable Node condition) {
        super(token);
        this.value = value;
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
    }
}
