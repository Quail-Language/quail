package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocTOCEntryNode extends DocTOCNode {

    public final String docString;

    public DocTOCEntryNode(Token token) {
        super(token);
        docString = token.getLexeme().replaceFirst("#\\?toc-entry\\s*", "");
    }

    @Override
    public String stringRepr() {
        return "TOC Entry: " + docString;
    }

}
