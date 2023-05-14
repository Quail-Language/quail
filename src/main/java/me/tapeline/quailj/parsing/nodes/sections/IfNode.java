package me.tapeline.quailj.parsing.nodes.sections;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.Collections;
import java.util.List;

public class IfNode extends Node {

    public List<BlockNode> branches;
    public BlockNode elseBranch;
    public List<Node> conditions;

    public IfNode(Token token, List<Node> conditions, List<BlockNode> branches, BlockNode elseBranch) {
        super(token);
        this.branches = branches;
        this.elseBranch = elseBranch;
        this.conditions = conditions;
    }

    public IfNode(Token token, Node condition, BlockNode branch) {
        super(token);
        this.branches = Collections.singletonList(branch);
        this.elseBranch = null;
        this.conditions = Collections.singletonList(condition);
    }

    public IfNode(Token token, Node condition, BlockNode branch, BlockNode elseBranch) {
        super(token);
        this.branches = Collections.singletonList(branch);
        this.elseBranch = elseBranch;
        this.conditions = Collections.singletonList(condition);
    }

    @Override
    public String stringRepr() {
        return "if{" +
                TextUtils.nodeListToStringRepr(conditions) + " " +
                TextUtils.blockListToStringRepr(branches) + " " +
                (elseBranch != null? elseBranch.stringRepr() : "null") + "}";
    }

}
