package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocHtmlNode extends DocNode {

    public final String html;

    public DocHtmlNode(Token token) {
        super(token);
        html = token.getLexeme().replaceFirst("#\\?html\\s*", "");
    }

    @Override
    public String stringRepr() {
        return "Html: " + html;
    }

}
