package me.tapeline.quailj.parsing.nodes.sections;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.List;

public class BlockNode extends Node {

    public final List<Node> nodes;

    public BlockNode(Token token, List<Node> nodes) {
        super(token);
        this.nodes = nodes;
    }

    @Override
    public String stringRepr() {
        return "block" + TextUtils.nodeListToStringRepr(nodes);
    }

}
