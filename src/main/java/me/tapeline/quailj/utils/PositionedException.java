package me.tapeline.quailj.utils;

public class PositionedException extends Exception {

    private int character;
    private int line;
    private int length;

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
