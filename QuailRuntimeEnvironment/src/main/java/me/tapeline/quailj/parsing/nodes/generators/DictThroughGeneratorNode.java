package me.tapeline.quailj.parsing.nodes.generators;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;


public class DictThroughGeneratorNode extends Node {

    public final Node key;
    public final Node value;
    public final VariableNode iterator;
    public final RangeNode range;
    public final @Nullable Node condition;

    public DictThroughGeneratorNode(Token token, Node key, Node value,
                                    VariableNode iterator,
                                    RangeNode range,
                                    @Nullable Node condition) {
        super(token);
        this.key = key;
        this.value = value;
        this.iterator = iterator;
        this.range = range;
        this.condition = condition;
    }

    @Override
    public String stringRepr() {
        return "gendict{" + key.stringRepr() + " " +
                value.stringRepr() + " " +
                iterator.stringRepr() + " " +
                range.stringRepr() + " " +
                (condition != null? condition.stringRepr() : "null") + "}";
    }

}
