package me.tapeline.quailj.parsing;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.utils.PositionedException;

/**
 * Represents an error which could be thrown while parsing
 * @author Tapeline
 */
public class ParserException extends PositionedException {

    /**
     * Erroneous source code
     */
    private String code;

    public ParserException(String message, Token token) {
        super(message, token.getCharacter(), token.getLine(), token.getLength());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
