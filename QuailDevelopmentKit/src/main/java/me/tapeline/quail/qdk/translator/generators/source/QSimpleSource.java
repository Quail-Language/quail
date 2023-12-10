package me.tapeline.quail.qdk.translator.generators.source;

import me.tapeline.quailj.lexing.Token;

public class QSimpleSource extends QSourceGenerator {

    private final String[] imports;
    private final String[] lines;
    private final Token token;

    public QSimpleSource(Token token, String[] imports, String... lines) {
        this.imports = imports;
        this.lines = lines;
        this.token = token;
    }

    public QSimpleSource(Token token, String... lines) {
        this.lines = lines;
        this.imports = new String[0];
        this.token = token;
    }

    @Override
    public String[] getImports() {
        return imports;
    }

    @Override
    public String[] getLines() {
        return lines;
    }

    @Override
    public int line() {
        return token.getLine();
    }

    @Override
    public int character() {
        return token.getCharacter();
    }

    @Override
    public int length() {
        return token.getLength();
    }

    @Override
    public String getRuntimeStrikerParameters() {
        return character() + ", " + line() + ", " + length();
    }


}
