package me.tapeline.quailj.parsing.nodes.effects;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.Locale;

public class UseNode extends Node {

    public String path;
    public String var;

    public UseNode(Token token, String path, String var) {
        super(token);
        this.path = path;
        this.var = var;
    }

    @Override
    public String stringRepr() {
        return "use{" + path + " " + var + "}";
    }

}
