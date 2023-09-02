package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocTOCHtmlNode extends DocTOCNode {

    public String html;

    public DocTOCHtmlNode(Token token) {
        super(token);
        html = token.getLexeme().replaceFirst("#\\?toc-html\\s*", "");
    }

    @Override
    public String stringRepr() {
        return "TOC Html: " + html;
    }

}
