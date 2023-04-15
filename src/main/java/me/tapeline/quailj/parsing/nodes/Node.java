package me.tapeline.quailj.parsing.nodes;

import me.tapeline.quailj.lexing.Token;

public class Node {

    protected Token token;

    public Node(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

}
