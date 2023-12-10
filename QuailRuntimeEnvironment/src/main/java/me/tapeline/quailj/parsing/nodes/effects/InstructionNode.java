package me.tapeline.quailj.parsing.nodes.effects;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.Locale;

public class InstructionNode extends Node {

    public InstructionNode(Token token) {
        super(token);
    }

    @Override
    public String stringRepr() {
        return token.getLexeme().toLowerCase(Locale.ROOT);
    }

}
