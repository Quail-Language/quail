package me.tapeline.quailj.runtime.std.cli.argparser;

import me.tapeline.quailj.lexing.LexerException;

import java.util.ArrayList;
import java.util.List;

import static me.tapeline.quailj.runtime.std.cli.argparser.ArgTokenType.*;

public class ArgLexer {

    private final String sourceCode;
    private final List<ArgToken> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int startOfCurrentLine = 0;
    private int line = 1;

    public ArgLexer(String code) {
        sourceCode = code;
    }

    private void error(String message) throws LexerException {
        throw new LexerException(
                message,
                line - 1,
                start,
                current - start,
                sourceCode
        );
    }

    private boolean reachedEnd() {
        return current >= sourceCode.length();
    }

    private char next() {
        return sourceCode.charAt(current++);
    }

    private void addToken(ArgTokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new ArgToken(type, text));
    }

    private void addStringToken(String contents) {
        tokens.add(new ArgToken(STRING, contents));
    }

    private boolean match(char expected) {
        if (reachedEnd()) return false;
        if (sourceCode.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private boolean match(String expected) {
        if (reachedEnd()) return false;

        if (!sourceCode.substring(current).startsWith(expected)) return false;

        current += expected.length();
        return true;
    }

    private char peek() {
        if (reachedEnd()) return '\0';
        return sourceCode.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= sourceCode.length()) return '\0';
        return sourceCode.charAt(current + 1);
    }

    private int findNextSignificantIndex() {
        int offset = 0;
        while (current + offset < sourceCode.length()) {
            if (isSignificant(sourceCode.charAt(current + offset)))
                return offset;
            else
                offset++;
        }
        return -1;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isSignificant(char c) {
        return c != '\n' && c != '\t' && c != '\r' && c != ' ';
    }

    public List<ArgToken> scan() throws LexerException {
        while (!reachedEnd()) {
            start = current;
            scanToken();
        }
        return tokens;
    }

    private void scanToken() throws LexerException {
        char c = next();
        switch (c) {
            case '-': {
                if (match('-')) {
                    scanId();
                    addToken(KEYWORD_FLAG);
                } else {
                    scanId();
                    addToken(BOOL_FLAG);
                }
                break;
            }
            case '+': {
                scanId();
                addToken(BOOL_FLAG);
                break;
            }
            case '=': {
                addToken(EQUALS);
                break;
            }
            case '\n':
                line++;
                startOfCurrentLine = current;
                break;
            case '"': scanString(); addToken(STRING); break;
            case ' ':
            case '\r':
            case '\t':
                break;
            default:
                if (isDigit(c)) {
                    scanNumber();
                    addToken(NUMBER);
                }
                else if (isAlpha(c)) {
                    scanId();
                    addToken(ARGUMENT);
                }
                else
                    error("Unexpected character " + c);
                break;
        }
    }

    private void scanString() throws LexerException {
        StringBuilder content = new StringBuilder();
        while (peek() != '"' && !reachedEnd()) {
            if (match("\\\\")) {
                content.append('\\');
                continue;
            }
            if (match("\\\"")) {
                content.append("\"");
                continue;
            }
            if (match("\\n")) {
                content.append("\n");
                continue;
            }
            if (match("\\t")) {
                content.append("\t");
                continue;
            }
            if (match("\\r")) {
                content.append("\r");
                continue;
            }
            if (peek() == '\n') {
                line++;
                startOfCurrentLine = current;
                content.append('\n');
            }
            content.append(next());
        }
        if (reachedEnd()) {
            error("Unexpected string end");
            return;
        }
        next();
    }

    private void scanNumber() {
        while (isDigit(peek())) next();

        if (peek() == '.' && isDigit(peekNext())) {
            next();
            while (isDigit(peek())) next();
        }
    }

    private String scanId() {
        while (isAlphaNumeric(peek())) next();
        String content = sourceCode.substring(start, current);
        if (content.equalsIgnoreCase("false") ||
                content.equalsIgnoreCase("true")) {
            addToken(BOOL);
            return null;
        }
        return sourceCode.substring(start, current);
    }

}
