package me.tapeline.quailj.utils;

/**
 * An exception that has positions in code.
 * @author Tapeline
 * @see me.tapeline.quailj.lexing.LexerException
 * @see me.tapeline.quailj.parsing.ParserException
 */
public class PositionedException extends Exception {

    /**
     * Character position in the line of error
     */
    private int character;

    /**
     * Error line
     */
    private int line;

    /**
     * Error length
     */
    private int length;

    /**
     * Constructs PositionedException with given values
     * @param message error message
     * @param character error character position
     * @param line error line position
     * @param length error length
     */
    public PositionedException(String message, int character, int line, int length) {
        super(message);
        this.character = character;
        this.line = line;
        this.length = length;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
