package me.tapeline.quailj.parsing.nodes;

import me.tapeline.quailj.lexing.Token;

public abstract class Node {

    protected Token token;

    public Node(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public abstract String stringRepr();

}
