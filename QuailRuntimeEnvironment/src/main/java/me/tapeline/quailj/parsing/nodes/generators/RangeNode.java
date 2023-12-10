package me.tapeline.quailj.parsing.nodes.generators;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class RangeNode extends Node {

    public final Node rangeStart;
    public final Node rangeEnd;
    public final @Nullable Node rangeStep;
    public final boolean isIncluding;

    public RangeNode(Token token, Node rangeStart, Node rangeEnd, @Nullable Node rangeStep) {
        this(token, rangeStart, rangeEnd, rangeStep, token.getLexeme().contains("+"));
    }

    public RangeNode(Token token, Node rangeStart, Node rangeEnd, @Nullable Node rangeStep, boolean isIncluding) {
        super(token);
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.rangeStep = rangeStep;
        this.isIncluding = isIncluding;
    }

    @Override
    public String stringRepr() {
        return "range{" + rangeStart.stringRepr() + " " +
                rangeEnd.stringRepr() + " " +
                (rangeStep != null? rangeStep.stringRepr() : "null") + "}";
    }

}
