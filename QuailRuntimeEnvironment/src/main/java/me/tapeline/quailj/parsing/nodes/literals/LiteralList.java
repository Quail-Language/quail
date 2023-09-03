package me.tapeline.quailj.parsing.nodes.literals;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class LiteralList extends Node {

    public final List<Node> values;

    public LiteralList(Token token, List<Node> values) {
        super(token);
        this.values = values;
    }

    @Override
    public String stringRepr() {
        return "list" + TextUtils.nodeListToStringRepr(values);
    }

}
