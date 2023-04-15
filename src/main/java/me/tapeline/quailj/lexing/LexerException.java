package me.tapeline.quailj.lexing;

import me.tapeline.quailj.utils.PositionedException;

public class LexerException extends PositionedException {

    private String code;

    public LexerException(String message, int character, int line, int length, String code) {
        super(message, character, line, length);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
