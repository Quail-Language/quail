package me.tapeline.quailj.lexing;

import me.tapeline.quailj.utils.PositionedException;

/**
 * An exception that will be thrown when Lexer finds
 * a grammatical mistake.
 * @author Tapeline
 * @see Lexer
 */
public class LexerException extends PositionedException {

    /**
     * Source code that contains the mistake
     */
    private String code;

    /**
     * Constructs a LexerException with given data
     * @param message error message
     * @param character where mistake has been found (character in line)
     * @param line where mistake has been found (line)
     * @param length length of mistaken part
     * @param code source code with the mistake
     */
    public LexerException(String message, int character, int line, int length, String code) {
        super(message, character, line, length);
        this.code = code;
    }

    /**
     * @return source code with the mistake
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the source code with the mistake
     * @param code new source code
     */
    public void setCode(String code) {
        this.code = code;
    }

}
