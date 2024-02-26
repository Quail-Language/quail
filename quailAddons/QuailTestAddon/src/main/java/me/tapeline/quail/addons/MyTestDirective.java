package me.tapeline.quail.addons;

import me.tapeline.quailj.preprocessing.StringBoundariesMap;
import me.tapeline.quailj.preprocessing.directives.AbstractDirective;
import me.tapeline.quailj.preprocessing.directives.DirectiveArgument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyTestDirective extends AbstractDirective {

    @Override
    public String prefix() {
        return "testdirective";
    }

    @Override
    public List<DirectiveArgument> arguments() {
        return new ArrayList<>();
    }

    @Override
    public String applyDirective(String code, File scriptHome, StringBoundariesMap boundaries, Object... arguments) {
        System.out.println("Test directive applied");
        return code;
    }

}
