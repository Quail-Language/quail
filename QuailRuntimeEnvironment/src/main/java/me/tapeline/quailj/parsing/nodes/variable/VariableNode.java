package me.tapeline.quailj.parsing.nodes.variable;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class VariableNode extends Node {

    public final int accessModifiers;
    public final int[] modifiers;
    public final String name;
    public boolean isArgConsumer;
    public boolean isKwargConsumer;

    public VariableNode(Token token) {
        this(token, new int[0]);
    }

    public VariableNode(Token token, int[] modifiers) {
        super(token);
        this.modifiers = modifiers;
        if (modifiers.length >= 1)
            this.accessModifiers = modifiers[0];
        else
            this.accessModifiers = 0;
        this.name = token.getLexeme();
    }

    public VariableNode(Token token, int[] modifiers,
                        boolean isArgConsumer, boolean isKwargConsumer) {
        super(token);
        this.modifiers = modifiers;
        this.name = token.getLexeme();
        this.isArgConsumer = isArgConsumer;
        this.isKwargConsumer = isKwargConsumer;
        if (modifiers.length >= 1)
            this.accessModifiers = modifiers[0];
        else
            this.accessModifiers = 0;
    }

    @Override
    public String stringRepr() {
        return name;
    }

}
