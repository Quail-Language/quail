package me.tapeline.quailj.parsing.nodes.generators;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class DictForGeneratorNode extends Node {

    public Node key;
    public Node value;
    public List<VariableNode> iterators;
    public Node iterable;
    public @Nullable Node condition;

    public DictForGeneratorNode(Token token, Node key, Node value,
                                List<VariableNode> iterators,
                                Node iterable,
                                @Nullable Node condition) {
        super(token);
        this.key = key;
        this.value = value;
        this.iterators = iterators;
        this.iterable = iterable;
        this.condition = condition;
    }

    @Override
    public String stringRepr() {
        return "gendict{" + key.stringRepr() + " " +
                value.stringRepr() + " " +
                TextUtils.iteratorsToStringRepr(iterators) + " " +
                iterable.stringRepr() + " " +
                (condition != null? condition.stringRepr() : "null") + "}";
    }

}
