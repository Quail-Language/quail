package me.tapeline.quail.qdk.translator.generators.source;

public abstract class QSourceGenerator {

    public abstract String[] getImports();
    public abstract String[] getLines();

    public abstract int line();
    public abstract int character();
    public abstract int length();
    public abstract String getRuntimeStrikerParameters();

}
