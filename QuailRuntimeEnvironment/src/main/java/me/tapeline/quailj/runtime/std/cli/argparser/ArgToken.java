package me.tapeline.quailj.runtime.std.cli.argparser;

public class ArgToken {

    private String lexeme;
    private ArgTokenType type;

    public ArgToken( ArgTokenType type, String lexeme) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public ArgTokenType getType() {
        return type;
    }

}
