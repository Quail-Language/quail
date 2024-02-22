package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocSeeNode extends DocNode {

    public final String see;

    public DocSeeNode(Token token) {
        super(token);
        see = token.getLexeme().replaceFirst("#\\?see", "").trim();
    }

    @Override
    public String stringRepr() {
        return "See: " + see;
    }

}
