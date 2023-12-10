package me.tapeline.quailj.parsing.nodes.sections;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class ThroughNode extends Node {

    public final Node rangeStart;
    public final Node rangeEnd;
    public final @Nullable Node rangeStep;
    public final String iterator;
    public final Node code;
    public final boolean isIncluding;

    public ThroughNode(Token token, Node rangeStart, Node rangeEnd,
                       @Nullable Node rangeStep, String iterator, Node code) {
        this(token, rangeStart, rangeEnd, rangeStep, iterator, code, false);
    }

    public ThroughNode(Token token, Node rangeStart, Node rangeEnd,
                       @Nullable Node rangeStep, String iterator,
                       Node code, boolean isIncluding) {
        super(token);
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.rangeStep = rangeStep;
        this.iterator = iterator;
        this.code = code;
        this.isIncluding = isIncluding;
    }

    public ThroughNode(Token token, Node rangeStart, Node rangeEnd, String iterator, Node code) {
        this(token, rangeStart, rangeEnd, null, iterator, code);
    }

    @Override
    public String stringRepr() {
        return "through{" + rangeStart.stringRepr() + " " +
                rangeEnd.stringRepr() + " " +
                (rangeStep != null? rangeStep.stringRepr() : "null") +
                " " + iterator + " " + code.stringRepr() + "}";
    }

}
