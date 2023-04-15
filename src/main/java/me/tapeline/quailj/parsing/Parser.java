package me.tapeline.quailj.parsing;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;

import java.util.List;

import static me.tapeline.quailj.lexing.TokenType.*;

public class Parser {

    private final List<Token> tokens;
    private final String sourceCode;
    private int pos = 0;

    public Parser(String code, List<Token> tokens) {
        this.tokens = tokens;
        this.sourceCode = code;
    }

    private void error(String message) throws ParserException {
        throw new ParserException(
                message,
                tokens.get(pos)
        );
    }

    private void error(Token token, String message) throws ParserException {
        throw new ParserException(
                message,
                token
        );
    }

    private boolean reachedEnd() {
        return pos >= tokens.size();
    }

    private boolean reachedEndAt(int pos) {
        return pos >= tokens.size();
    }

    private boolean reachedEndOffset(int offset) {
        return reachedEndAt(pos + offset);
    }

    private Token current() {
        if (reachedEnd()) return null;
        return tokens.get(pos);
    }

    private Token getNext() {
        if (reachedEndOffset(1)) return null;
        return tokens.get(pos + 1);
    }

    private Token getNext(int offset) {
        if (reachedEndOffset(offset)) return null;
        return tokens.get(pos + offset);
    }

    private Token match(TokenType type) {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        if (type == current.getType()) {
            pos += increment + 1;
            return current;
        }
        return null;
    }

    private Token matchExactly(TokenType type) {
        if (reachedEnd()) return null;
        Token current = tokens.get(pos);
        if (type.equals(current.getType())) {
            pos++;
            return current;
        }
        return null;
    }

    private Token matchMultiple(TokenType... types) {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        for (TokenType acceptable : types) {
            if (acceptable == current.getType()) {
                pos += increment + 1;
                return current;
            }
        }
        return null;
    }

    private Token matchSameLine(TokenType type) {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        if (type == current.getType()) {
            pos += increment + 1;
            return current;
        }
        return null;
    }

    private Token consumeAny() {
        if (reachedEnd()) return null;
        return tokens.get(pos++);
    }

    private Token consumeSignificant() {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        pos += increment + 1;
        return current;
    }

    private Token toNextSignificant() {
        if (reachedEnd()) return null;
        int increment = 0;
        while (true) {
            Token next = getNext(increment);
            if (next == null) return null;
            if (next.getType() == EOL || next.getType() == TAB) increment++;
            else break;
        }
        Token current = getNext(increment);
        if (current == null) return null;
        pos += increment;
        return current;
    }

    private Token require(TokenType type, String message) throws ParserException {
        Token token = match(type);
        if (reachedEnd())
            error(message == null? "Expected " + type.toString() + " but file ended" : message);
        else if (token == null && getNext() != null)
            error(message == null? "Expected " + type.toString() +
                    " but found " + getNext().getType() : message);
        else
            error(message == null? "Expected " + type.toString() + " but found none" : message);
        return token;
    }

}
