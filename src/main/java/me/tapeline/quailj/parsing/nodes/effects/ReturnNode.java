package me.tapeline.quailj.parsing.nodes.effects;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.Locale;

public class ReturnNode extends Node {

    public @Nullable Node value;

    public ReturnNode(Token token) {
        this(token, null);
    }

    public ReturnNode(Token token, @Nullable Node value) {
        super(token);
        this.value = value;
    }

    @Override
    public String stringRepr() {
        return "return{" + value.stringRepr() + "}";
    }

}
