package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.preprocessing.StringBoundariesMap;

import java.io.File;
import java.util.List;

/**
 * ABC for directives - textual representations of preprocessor rules.
 * Learn more at Quail Specification Chapter III
 * @author Tapeline
 */
public abstract class AbstractDirective {

    /**
     * @return directive's prefix
     */
    public abstract String prefix();

    /**
     * @return directive's argument list
     */
    public abstract List<DirectiveArgument> arguments();

    /**
     * Executes all actions related to directive behaviour
     * and return modified code.
     * @param code source code to be modified
     * @param scriptHome script's parent directory
     * @param boundaries map of all strings in code
     * @param arguments arguments provided to directive
     * @return modified code
     */
    public abstract String applyDirective(String code,
                                          File scriptHome,
                                          StringBoundariesMap boundaries,
                                          Object... arguments);

}
