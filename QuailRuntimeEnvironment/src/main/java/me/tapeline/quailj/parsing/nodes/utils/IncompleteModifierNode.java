package me.tapeline.quailj.parsing.nodes.utils;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class IncompleteModifierNode extends Node {

    public final int[] modifiers;

    public IncompleteModifierNode(int[] modifiers) {
        super(Token.UNDEFINED);
        this.modifiers = modifiers;
    }


    @Override
    public String stringRepr() {
        return "incompletemodifiernode";
    }

}
