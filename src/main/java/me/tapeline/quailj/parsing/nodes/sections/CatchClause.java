package me.tapeline.quailj.parsing.nodes.sections;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.parsing.nodes.Node;

public class CatchClause {

    public @Nullable Node instance;
    public String var;
    public Node code;

    public CatchClause(@Nullable Node instance, String var, Node code) {
        this.instance = instance;
        this.var = var;
        this.code = code;
    }

    public String stringRepr() {
        return "catch{" + (instance != null? instance.stringRepr() : "any") + " " +
                var + " " + code.stringRepr() + "}";
    }

}
