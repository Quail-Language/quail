package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.preprocessing.StringBoundariesMap;

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

    /**
     * Passed arguments
     */
    public Object[] args;

    /**
     * Constructs ScannedDirective with provided
     * directive arguments
     * @param args directive arguments
     */
    public ScannedDirective(Object... args) {
        this.args = args;
    }

    /**
     * Applies directive with given arguments to given code
     * considering existing string boundaries in code and
     * returns modified code
     * @param code given source code
     * @param boundaries string boundaries
     * @return modified code
     */
    public String apply(String code, StringBoundariesMap boundaries) {
        return directive().applyDirective(code, boundaries, args);
    }

}
