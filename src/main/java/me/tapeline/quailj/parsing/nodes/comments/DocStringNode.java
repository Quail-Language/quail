package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocStringNode extends DocNode {

    public String docString;

    public DocStringNode(Token token) {
        super(token);
        docString = token.getLexeme().replaceFirst("#\\?\\s*", "");
    }

    @Override
    public String stringRepr() {
        return "DocString: " + docString;
    }

}
