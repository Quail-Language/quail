package me.tapeline.quail.preprocessing.directives;

import me.tapeline.quail.preprocessing.StringBoundariesMap;

public abstract class ScannedDirective {

    public abstract AbstractDirective directive();

    public Object[] args;

    public ScannedDirective(Object... args) {
        this.args = args;
    }

    public String apply(String code, StringBoundariesMap boundaries) {
        return directive().applyDirective(code, boundaries, args);
    }

}
