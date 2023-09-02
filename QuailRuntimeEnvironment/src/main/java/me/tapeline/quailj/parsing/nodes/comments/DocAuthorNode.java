package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocAuthorNode extends DocNode {

    public String author;

    public DocAuthorNode(Token token) {
        super(token);
        author = token.getLexeme().replaceFirst("#\\?author", "");
    }

    @Override
    public String stringRepr() {
        return "Author: " + author;
    }

}
