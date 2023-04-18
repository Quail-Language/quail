package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenModifier;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class UnaryOperatorNode extends Node {

    public TokenType op;
    public TokenModifier mod;
    public Node value;

    public UnaryOperatorNode(Token token, Node value) {
        super(token);
        this.value = value;
        this.mod = token.getMod();
        this.op = token.getType();
    }

}
