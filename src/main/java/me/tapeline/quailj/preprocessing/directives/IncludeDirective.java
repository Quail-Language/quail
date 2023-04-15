package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.GlobalFlags;
import me.tapeline.quailj.preprocessing.StringBoundariesMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class IncludeDirective extends AbstractDirective {

    @Override
    public String prefix() {
        return "include";
    }

    @Override
    public List<DirectiveArgument> arguments() {
        return Collections.singletonList(DirectiveArgument.STRING);
    }

    @Override
    public String applyDirective(String code, StringBoundariesMap boundaries, Object... arguments) {
        try {
            String contents = FileUtils.readFileToString(
                    new File(((String) arguments[0])),
                    GlobalFlags.encoding
            ).replace("\r\n", "\n");
            return "#Included from " + ((String) arguments[0]) + "\n" + contents + "\n\n" + code;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
