package me.tapeline.quailj.parsing.nodes;

import me.tapeline.quailj.lexing.Token;

public abstract class Node {

    public long executionStart = -1;
    public long executionTime = -1;

    protected Token token;

    public Node(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public abstract String stringRepr();

}
