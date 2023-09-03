package me.tapeline.quailj.parsing.nodes.effects;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;


public class ReturnNode extends Node {

    public final @Nullable Node value;

    public ReturnNode(Token token) {
        this(token, null);
    }

    public ReturnNode(Token token, @Nullable Node value) {
        super(token);
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return "return{" + (value != null? value.stringRepr() : "null") + "}";
    }

}
