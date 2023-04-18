package me.tapeline.quailj.parsing.nodes.variable;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;

public class VariableNode extends Node {

    public int[] modifiers;

    public VariableNode(Token token) {
        this(token, new int[0]);
    }

    public VariableNode(Token token, int[] modifiers) {
        super(token);
        this.modifiers = modifiers;
    }

}
