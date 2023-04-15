package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.preprocessing.StringBoundariesMap;

import java.util.List;

public abstract class AbstractDirective {

    public abstract String prefix();

    public abstract List<DirectiveArgument> arguments();

    public abstract String applyDirective(String code, StringBoundariesMap boundaries, Object... arguments);

}
