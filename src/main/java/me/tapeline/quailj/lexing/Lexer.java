package me.tapeline.quailj.lexing;

import me.tapeline.quailj.GlobalFlags;

import java.util.ArrayList;
import java.util.List;
import static me.tapeline.quailj.lexing.TokenModifier.*;
import static me.tapeline.quailj.lexing.TokenType.*;

/**
 * A lexical analyzer. Scans provided source code and retrieves
 * tokens from it by defined grammar rules.
 * @author Tapeline
 */
public class Lexer {

    /**
     * Source code for scan
     */
    private final String sourceCode;

    /**
     * List of scanned tokens
     */
    private final List<Token> tokens = new ArrayList<>();

    /**
     * Character index of start of currently scanning token
     */
    private int start = 0;

    /**
     * Current character index
     */
    private int current = 0;

    /**
     * Index of character that starts current line
     */
    private int startOfCurrentLine = 0;

    /**
     * Current line
     * */
    private int line = 1;

    /**
     * Constructs a new Lexer for provided source code
     * @param code source code
     */
    public Lexer(String code) {
        sourceCode = code;
    }

    /**
     * Assembles and throws a LexerException with current positions and given message
     * @param message error message
     * @throws LexerException assembled exception
     * @see LexerException
     */
    private void error(String message) throws LexerException {
        throw new LexerException(
                message,
                line - 1,
                start,
                current - start,
                sourceCode
        );
    }

    /**
     * @return whether lexer reached end of code
     */
    private boolean reachedEnd() {
        return current >= sourceCode.length();
    }

    /**
     * Get current char and move current pos forward
     * @return current char
     */
    private char next() {
        return sourceCode.charAt(current++);
    }

    /**
     * Adds given token
     * @param type token that should be added
     */
    private void addToken(TokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(type, text, line, start - startOfCurrentLine, current - start));
    }

    /**
     * Adds given string token with given data
     * @param contents string contents
     */
    private void addStringToken(String contents) {
        tokens.add(new Token(LITERAL_STR, contents, line,
                start - startOfCurrentLine, current - start));
    }

    /**
     * Adds given token with matrix modifier
     * @param type token that should be added
     */
    private void addMatrixToken(TokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(MATRIX_MOD,
                type, text, line, start - startOfCurrentLine, current - start));
    }

    /**
     * Adds given token with array modifier
     * @param type token that should be added
     */
    private void addArrayToken(TokenType type) {
        String text = sourceCode.substring(start, current);
        tokens.add(new Token(ARRAY_MOD,
                type, text, line, start - startOfCurrentLine, current - start));
    }

    /**
     * Matches expected character in code from current position
     * @param expected expected character
     * @return whether code from current position starts with expected character
     */
    private boolean match(char expected) {
        if (reachedEnd()) return false;
        if (sourceCode.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * Matches expected string in code from current position
     * @param expected expected string
     * @return whether code from current position starts with expected string
     */
    private boolean match(String expected) {
        if (reachedEnd()) return false;

        if (!sourceCode.substring(current).startsWith(expected)) return false;

        current += expected.length();
        return true;
    }

    /**
     * Gets current character
     * @return \0 if current char is out of code bounds. If not - target char
     */
    private char peek() {
        if (reachedEnd()) return '\0';
        return sourceCode.charAt(current);
    }

    /**
     * Gets next character
     * @return \0 if next char is out of code bounds. If not - target char
     */
    private char peekNext() {
        if (current + 1 >= sourceCode.length()) return '\0';
        return sourceCode.charAt(current + 1);
    }

    /**
     * @param c target character
     * @return whether the character is a digit
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * @param c target character
     * @return whether the character is alphabetical
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    /**
     * @param c target character
     * @return whether the character is alphanumeric
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Checks for short assign operators and adds binary op token
     * @param type binary operator token
     */
    private void addBinaryOp(TokenType type) {
        if (match('=')) {
            String text = sourceCode.substring(start, current);
            switch (type) {
                case PLUS: addToken(SHORT_PLUS); return;
                case MINUS: addToken(SHORT_MINUS); return;
                case MULTIPLY: addToken(SHORT_MULTIPLY); return;
                case DIVIDE: addToken(SHORT_DIVIDE); return;
                case INTDIV: addToken(SHORT_INTDIV); return;
                case MODULO: addToken(SHORT_MODULO); return;
                case POWER: addToken(SHORT_POWER); return;
            }
        }
        addToken(type);
    }

    /**
     * Returns list of scanned tokens or
     * throws an exception if there is a
     * syntax error.
     *
     * @return scanned tokens
     * @throws LexerException if any grammatical mistakes are in code
     */
    public List<Token> scan() throws LexerException {
        while (!reachedEnd()) {
            start = current;
            scanToken();
        }
        return tokens;
    }

    /**
     * Scans, identifies and adds one token
     * @throws LexerException if token is unknown
     */
    private void scanToken() throws LexerException {
        char c = next();
        switch (c) {
            case '(': addToken(LPAR); break;
            case ')': addToken(RPAR); break;
            case '{':
                if (match("...}"))
                    addToken(KWARG_CONSUMER);
                else
                    scanCurlyBrace();
                break;
            case '}': addToken(RCPAR); break;
            case '[': scanBracket(); break;
            case ']': addToken(RSPAR); break;

            case ',': addToken(COMMA); break;

            case '\'':
                match('s');
                addToken(DOT);
                break;
            case '.':
                if (match(".."))
                    addToken(CONSUMER);
                else
                    addToken(DOT);
                break;

            case '-':
                if (match('>'))
                    addToken(LAMBDA_ARROW);
                else
                    addBinaryOp(MINUS);
                break;
            case '+': addBinaryOp(PLUS); break;
            case '/':
                if (match('/'))
                    addBinaryOp(INTDIV);
                else
                    addBinaryOp(DIVIDE);
                break;
            case '*': addBinaryOp(MULTIPLY); break;
            case '%': addBinaryOp(MODULO); break;
            case '^': addBinaryOp(POWER); break;
            case '>':
                if (match('='))
                    addToken(GREATER_EQUAL);
                else
                    addToken(GREATER);
                break;
            case '<':
                if (match('='))
                    addToken(LESS_EQUAL);
                else
                    addToken(LESS);
                break;
            case '=':
                if (match('='))
                    addToken(EQUALS);
                else
                    addToken(ASSIGN);
                break;
            case '!':
                if (match('='))
                    addToken(NOT_EQUALS);
                else
                    addToken(NOT);
                break;
            case '&':
                if (match('&'))
                    addToken(AND);
                else
                    error("Unknown lexeme &. Did you mean &&?");
                break;
            case '|':
                if (match('|'))
                    addToken(OR);
                else
                    addToken(PILLAR);
                break;
            case ':':
                if (match('+'))
                    addToken(RANGE_INCLUDE);
                else
                    addToken(RANGE);
                break;
            case '#':
                scanComment();
                break;
            case ';':
            case '\n':
                addToken(EOL);
                line++;
                startOfCurrentLine = current;
                break;
            case '"': scanString(); break;
            case ' ':
                if (match("   "))
                    addToken(TAB);
                break;
            case '\r':
                break;
            case '\t':
                addToken(TAB);
                break;
            default:
                if (isDigit(c))
                    scanNumber();
                else if (isAlpha(c))
                    scanId();
                else
                    error("Unexpected character " + c);
                break;
        }
    }

    /**
     * Scans for square [ bracket and array operators
     */
    private void scanBracket() {
        String currentOp = null;
        for (String op : ops)
            if (sourceCode.substring(current).startsWith(op + "]")) {
                currentOp = op;
                break;
            }
        if (currentOp == null)
            addToken(LSPAR);
        else {
            match(currentOp + "]");
            addArrayToken(stringToOps.get(currentOp));
        }
    }

    /**
     * Scans for curly { brace and matrix operators
     */
    private void scanCurlyBrace() {
        String currentOp = null;
        for (String op : ops)
            if (sourceCode.substring(current).startsWith(op + "}")) {
                currentOp = op;
                break;
            }
        if (currentOp == null)
            addToken(LCPAR);
        else {
            match(currentOp + "}");
            addMatrixToken(stringToOps.get(currentOp));
        }
    }

    /**
     * Scans a comment
     */
    private void scanComment() {
        if (!GlobalFlags.ignoreDocs && peek() == '?') {
            while (peek() != '\n' && !reachedEnd())
                next();
            addToken(DOCS);
        }
        while (peek() != '\n' && !reachedEnd())
            next();
    }

    /**
     * Scans a string
     * @throws LexerException if string has not been closed properly
     */
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
        addStringToken(content.toString());
    }

    /**
     * Scans a number token
     */
    private void scanNumber() {
        while (isDigit(peek())) next();

        if (peek() == '.' && isDigit(peekNext())) {
            next();
            while (isDigit(peek())) next();
        }

        addToken(LITERAL_NUM);
    }

    /**
     * Scans keyword or identifier
     */
    private void scanId() {
        while (isAlphaNumeric(peek())) next();
        String text = sourceCode.substring(start, current);
        if (text.equals("stop"))
            if (match(" when")) {
                addToken(CONTROL_STOP_WHEN);
                return;
            }
        TokenType type = keywords.get(text);
        if (type == null) type = VAR;
        addToken(type);
    }

}
