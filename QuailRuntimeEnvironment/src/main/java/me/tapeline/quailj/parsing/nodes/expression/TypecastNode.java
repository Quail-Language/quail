package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class TypecastNode extends Node {

    public final int cast;
    public final Node expr;

    public TypecastNode(Token token, int cast, Node expr) {
        super(token);
        this.cast = cast;
        this.expr = expr;
    }

    @Override
    public String stringRepr() {
        return "cast{" + cast + " " + expr.stringRepr() + "}";
    }

}
