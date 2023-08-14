package me.tapeline.quailj.preprocessing.directives;

/**
 * Definitions for different directive argument types
 * Learn more at Quail Specification Chapter III
 * @author Tapeline
 */
public enum DirectiveArgument {

    /**
     * String argument. Could be passed either with double quotes
     * or without
     */
    STRING,

    /**
     * Integer argument
     */
    INT,

    /**
     * Boolean argument. Could be passed as: true, false, 0, 1, yes, no, enable, disable
     * Case-insensitive
     */
    BOOL,

    /**
     * Code argument. A string, but it can contain whitespaces without
     * necessity to quote. But code argument can be only the last
     * argument and there cannot be more than one of them in one directive
     */
    CODE

}
