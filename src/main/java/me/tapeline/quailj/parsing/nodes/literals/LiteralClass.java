package me.tapeline.quailj.parsing.nodes.literals;

import com.sun.istack.internal.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.utils.TextUtils;

import java.util.HashMap;
import java.util.List;

public class LiteralClass extends Node {

    public String name;
    public @Nullable Node like;
    public HashMap<String, Node> contents;
    public HashMap<String, LiteralFunction> methods;
    public List<Node> initialize;

    public LiteralClass(Token token,
                        String name,
                        @Nullable Node like,
                        HashMap<String, Node> contents,
                        HashMap<String, LiteralFunction> methods,
                        List<Node> initialize) {
        super(token);
        this.name = name;
        this.like = like;
        this.contents = contents;
        this.methods = methods;
        this.initialize = initialize;
    }

    @Override
    public String stringRepr() {
        return "class{" + name + " " +
                (like != null? like.stringRepr() : "null") + " " +
                TextUtils.nodeMapToStringRepr(contents) + " " +
                TextUtils.methodMapToStringRepr(methods) + " " +
                TextUtils.nodeListToStringRepr(initialize) + "}";
    }

}
