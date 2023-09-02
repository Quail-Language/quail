package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.preprocessing.StringBoundariesMap;

import java.io.File;

/**
 * An instance of existing directive in code
 * Learn more at Quail Specification Chapter III
 * @see AbstractDirective
 * @author Tapeline
 */
public abstract class ScannedDirective {

    /**
     * @return directive's type
     */
    public abstract AbstractDirective directive();

    public Object[] args;

    public ScannedDirective(Object... args) {
        this.args = args;
    }

    /**
     * Applies directive with given arguments to given code
     * considering existing string boundaries in code,
     * its directory location and returns modified code
     * @param code given source code
     * @param boundaries string boundaries
     * @return modified code
     */
    public String apply(String code, File scriptHome, StringBoundariesMap boundaries) {
        return directive().applyDirective(code, scriptHome, boundaries, args);
    }

}
