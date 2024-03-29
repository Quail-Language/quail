package me.tapeline.quailj.parsing.nodes.sections;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class ForNode extends Node {

    public final List<String> iterators;
    public final Node iterable;
    public final Node code;

    public ForNode(Token token, List<String> iterators, Node iterable, Node code) {
        super(token);
        this.iterators = iterators;
        this.iterable = iterable;
        this.code = code;
    }

    @Override
    public String stringRepr() {
        return "for{" + iterators + " " + iterable.stringRepr() + " " + code.stringRepr() + "}";
    }

}
