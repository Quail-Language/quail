package me.tapeline.quailj.parsing.nodes.variable;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class VariableNode extends Node {

    public int[] modifiers;
    public String name;
    public boolean isArgConsumer;
    public boolean isKwargConsumer;

    public VariableNode(Token token) {
        this(token, new int[0]);
    }

    public VariableNode(Token token, int[] modifiers) {
        super(token);
        this.modifiers = modifiers;
        this.name = token.getLexeme();
    }

    public VariableNode(Token token, int[] modifiers,
                        boolean isArgConsumer, boolean isKwargConsumer) {
        super(token);
        this.modifiers = modifiers;
        this.name = token.getLexeme();
        this.isArgConsumer = isArgConsumer;
        this.isKwargConsumer = isKwargConsumer;
    }

    @Override
    public String stringRepr() {
        return name;
    }

}
