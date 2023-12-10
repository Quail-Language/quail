package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class LiteralDict extends Node {

    public final List<Node> keys;
    public final List<Node> values;

    public LiteralDict(Token token, List<Node> keys, List<Node> values) {
        super(token);
        this.keys = keys;
        this.values = values;
    }

    @Override
    public String stringRepr() {
        return "dict{" + TextUtils.nodeListToStringRepr(keys) + " " +
                TextUtils.nodeListToStringRepr(values) + "}";
    }

}
