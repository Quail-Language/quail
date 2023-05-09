package me.tapeline.quailj.parsing.nodes.generators;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class RangeNode extends Node {

    public Node rangeStart;
    public Node rangeEnd;
    public @Nullable Node rangeStep;

    public RangeNode(Token token, Node rangeStart, Node rangeEnd, @Nullable Node rangeStep) {
        super(token);
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.rangeStep = rangeStep;
    }

    @Override
    public String stringRepr() {
        return "range{" + rangeStart.stringRepr() + " " +
                rangeEnd.stringRepr() + " " +
                (rangeStart != null? rangeStart.stringRepr() : "null") + "}";
    }

}
