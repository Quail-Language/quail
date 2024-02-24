package me.tapeline.quail.addons;

import me.tapeline.quailj.addons.QuailAddon;
import me.tapeline.quailj.parsing.annotation.Annotation;
import me.tapeline.quailj.preprocessing.directives.AbstractDirective;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;

import java.util.Arrays;
import java.util.List;

public class QuailTestAddon extends QuailAddon {

    @Override
    public String getName() {
        return "QuailTestAddon";
    }

    @Override
    public List<AbstractDirective> providedDirectives() {
        return Arrays.asList(
            new MyTestDirective()
        );
    }

    @Override
    public List<Annotation> providedAnnotations() {
        return Arrays.asList(
                new MyTestAnnotation()
        );
    }

    @Override
    public List<BuiltinLibrary> providedLibraries() {
        return Arrays.asList(
                new MyTestLibrary()
        );
    }

}
