package me.tapeline.quailj.parsing.nodes.effects;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.Locale;

public class EffectNode extends Node {

    public final Node value;

    public EffectNode(Token token, Node value) {
        super(token);
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return token.getLexeme().toLowerCase(Locale.ROOT) + "{" + value.stringRepr() + "}";
    }

}
