package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenModifier;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class BinaryOperatorNode extends Node {

    public TokenType op;
    public TokenModifier mod;
    public Node left;
    public Node right;

    public BinaryOperatorNode(Token token, Node left, Node right) {
        super(token);
        this.left = left;
        this.right = right;
        this.mod = token.getMod();
        this.op = token.getType();
    }

    @Override
    public String stringRepr() {
        return "op{" + left.stringRepr() + " " + op.toString() + " " + right.stringRepr() + "}";
    }

}
