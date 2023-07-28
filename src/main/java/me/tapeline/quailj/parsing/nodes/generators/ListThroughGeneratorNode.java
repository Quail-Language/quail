package me.tapeline.quailj.parsing.nodes.generators;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

public class ListThroughGeneratorNode extends Node {

    public Node value;
    public VariableNode iterator;
    public RangeNode range;
    public @Nullable Node condition;

    public ListThroughGeneratorNode(Token token, Node value,
                                    VariableNode iterator,
                                    RangeNode range,
                                    @Nullable Node condition) {
        super(token);
        this.value = value;
        this.iterator = iterator;
        this.range = range;
        this.condition = condition;
    }

    @Override
    public String stringRepr() {
        return "genlist{" + value.stringRepr() + " " +
                iterator.stringRepr() + " " +
                range.stringRepr() + " " +
                (condition != null? condition.stringRepr() : "null") + "}";
    }

}
