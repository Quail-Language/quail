package me.tapeline.quailj.lexing;

/**
 * Token - a grammatically meaningful word, number, symbol and
 * their sequences. Differentiates by TokenType and
 * is produced by Lexer
 * @author Tapeline
 * @see TokenType
 * @see Lexer
 */
public class Token {

    /**
     * Utility token
     * */
    public static Token UNDEFINED = new Token(TokenType.EOL, "\n", 1, 0, 1);

    /**
     * Type of this token
     */
    private final TokenType type;

    /**
     * String value of this token
     * (word, number, symbol, etc.)
     */
    private final String lexeme;

    /**
     * Line on which this token has been scanned
     */
    private final int line;

    /**
     * Character position in line on which this token has been scanned
     */
    private final int character;

    /**
     * Length of this token
     */
    private final int length;

    /**
     * Token modifier
     * @see TokenModifier
     */
    private TokenModifier mod = TokenModifier.SINGULAR_MOD;

    /**
     * Constructs Token with default modifier (SINGULAR_MOD)
     * @param type token's type
     * @param lexeme string value
     * @param line line position
     * @param character character position
     * @param length length of this token
     */
    public Token(TokenType type, String lexeme, int line, int character, int length) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.character = character;
        this.length = length;
    }

    /**
     * Constructs Token with provided values
     * @param mod token modifier
     * @param type token's type
     * @param lexeme string value
     * @param line line position
     * @param character character position
     * @param length length of this token
     * @see TokenModifier
     */
    public Token(TokenModifier mod, TokenType type, String lexeme, int line, int character, int length) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.character = character;
        this.length = length;
        this.mod = mod;
    }

    /**
     * @return type of this token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * @return lexeme of this token
     */
    public String getLexeme() {
        return lexeme;
    }

    /**
     * @return line position of this token
     */
    public int getLine() {
        return line;
    }

    /**
     * @return character position of this token
     */
    public int getCharacter() {
        return character;
    }

    /**
     * @return length of this token
     */
    public int getLength() {
        return length;
    }

    /**
     * Represents this token as a string:
     * if EOL then "EOL"
     * else "TOKEN_TYPE token_lexeme"
     * @return string representation
     */
    public String toString() {
        return type  + (type == TokenType.EOL? "" : " " + lexeme);
    }

    /**
     * @return modifier of this token
     */
    public TokenModifier getMod() {
        return mod;
    }

    /**
     * Creates a new token based on this and provided data.
     * Modifier, line position, character position and length
     * are preserved. Type and lexeme are given.
     * @param type new type
     * @param lexeme new lexeme
     * @return new Token
     */
    public Token derivativeFor(TokenType type, String lexeme) {
        return new Token(mod, type, lexeme, line, character, length);
    }

}
