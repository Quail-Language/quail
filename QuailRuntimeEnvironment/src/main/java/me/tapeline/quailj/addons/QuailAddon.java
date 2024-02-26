package me.tapeline.quailj.addons;

import me.tapeline.quailj.parsing.annotation.Annotation;
import me.tapeline.quailj.preprocessing.directives.AbstractDirective;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;

import java.util.List;

public abstract class QuailAddon {

    public abstract String getName();
    public abstract List<AbstractDirective> providedDirectives();
    public abstract List<Annotation> providedAnnotations();
    public abstract List<BuiltinLibrary> providedLibraries();
    public void customizeRuntime(Runtime runtime) {}

}
