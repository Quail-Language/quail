package me.tapeline.quail.lexing;

public class Token {

    public static Token UNDEFINED = new Token(TokenType.EOL, "\n", 1, 0, 1);

    private TokenType type;
    private final String lexeme;
    private int line;
    private int character;
    private int length;
    private TokenModifier mod = TokenModifier.SINGULAR_MOD;

    public Token(TokenType type, String lexeme, int line, int character, int length) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.character = character;
        this.length = length;
    }

    public Token(TokenModifier mod, TokenType type, String lexeme, int line, int character, int length) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.character = character;
        this.length = length;
        this.mod = mod;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    public int getLength() {
        return length;
    }

    public String toString() {
        return type  + (type == TokenType.EOL? "" : " " + lexeme);
    }

    public TokenModifier getMod() {
        return mod;
    }

}
