package me.tapeline.quailj.parsing.nodes.literals;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.expression.VarAssignNode;
import me.tapeline.quailj.utils.TextUtils;

import java.util.HashMap;
import java.util.List;

public class LiteralClass extends Node {

    public final String name;
    public final @Nullable Node like;
    public final List<VarAssignNode> contents;
    public final HashMap<String, LiteralFunction> methods;
    public final List<Node> initialize;

    public LiteralClass(Token token,
                        String name,
                        @Nullable Node like,
                        List<VarAssignNode> contents,
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
                TextUtils.nodeListToStringRepr(contents) + " " +
                TextUtils.methodMapToStringRepr(methods) + " " +
                TextUtils.nodeListToStringRepr(initialize) + "}";
    }

}
