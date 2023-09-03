package me.tapeline.quailj.parsing.nodes.comments;

import me.tapeline.quailj.lexing.Token;

public class DocBadgeNode extends DocNode {

    public String badgeString;
    public final String badgeColor;

    public DocBadgeNode(Token token) {
        super(token);
        badgeString = token.getLexeme().replaceFirst("#\\?badge", "");
        if (badgeString.startsWith("-")) {
            badgeColor = badgeString.substring(1, badgeString.indexOf(' '));
            badgeString = badgeString.substring(badgeString.indexOf(' ') + 1);
        } else {
            badgeColor = "default";
        }
    }

    @Override
    public String stringRepr() {
        return "DocBadge " + badgeColor + ": " + badgeString;
    }

}
