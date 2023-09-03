package me.tapeline.quailj.parsing.nodes.sections;

import org.jetbrains.annotations.Nullable;
import me.tapeline.quailj.parsing.nodes.Node;

public class CatchClause {

    public final @Nullable Node instance;
    public final String var;
    public final Node code;

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
