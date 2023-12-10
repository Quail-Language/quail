package me.tapeline.quailj.parsing.nodes.expression;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenModifier;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class UnaryOperatorNode extends Node {

    public final TokenType op;
    public final TokenModifier mod;
    public final Node value;

    public UnaryOperatorNode(Token token, Node value) {
        super(token);
        this.value = value;
        this.mod = token.getMod();
        this.op = token.getType();
    }

    @Override
    public String stringRepr() {
        return "op{" + op.toString() + " " + value.stringRepr() + "}";
    }

}
