package me.tapeline.quailj.parsing.nodes.sections;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class ThroughNode extends Node {

    public Node rangeStart;
    public Node rangeEnd;
    public @Nullable Node rangeStep;
    public String iterator;
    public Node code;

    public ThroughNode(Token token, Node rangeStart, Node rangeEnd,
                       @Nullable Node rangeStep, String iterator, Node code) {
        super(token);
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.rangeStep = rangeStep;
        this.iterator = iterator;
        this.code = code;
    }

    public ThroughNode(Token token, Node rangeStart, Node rangeEnd, String iterator, Node code) {
        this(token, rangeStart, rangeEnd, null, iterator, code);
    }

    @Override
    public String stringRepr() {
        return "through{" + rangeStart.stringRepr() + " " +
                rangeEnd.stringRepr() + " " +
                (rangeStart != null? rangeStart.stringRepr() : "null") +
                " " + iterator + " " + code.stringRepr() + "}";
    }

}
