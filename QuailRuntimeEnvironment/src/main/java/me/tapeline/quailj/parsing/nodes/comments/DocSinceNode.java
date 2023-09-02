package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocSinceNode extends DocNode {

    public String since;

    public DocSinceNode(Token token) {
        super(token);
        since = token.getLexeme().replaceFirst("#\\?since", "");
    }

    @Override
    public String stringRepr() {
        return "BeenSince: " + since;
    }

}
