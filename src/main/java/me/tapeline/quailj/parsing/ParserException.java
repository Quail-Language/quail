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

    public ParserException(String code, String message, Token token) {
        super(message, token.getCharacter(), token.getLine(), token.getLength());
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String formatError() {
        String[] lines = code.split("\n");
        StringBuilder sb = new StringBuilder();
        sb.append("At character ").append(getCharacter()).append(" line ").append(getLine()).append("\n");
        String linePrefix = " " + getLine() + " | ";
        sb.append(linePrefix);
        if (lines.length >= getLine())
            sb.append("!!! internal error !!! unable to display line !!!");
        else
            sb.append(lines[getLine()]);
        sb.append("\n");
        for (int i = 0; i < linePrefix.length(); i++)
            sb.append(" ");
        for (int i = 0; i < getLength(); i++)
            if (i == 0)
                sb.append("^");
            else
                sb.append("~");
        sb.append("\n");
        sb.append(getMessage());
        return sb.toString();
    }

}
