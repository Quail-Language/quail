package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.GlobalFlags;
import me.tapeline.quailj.preprocessing.StringBoundariesMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Include directive. Inserts content of given file at the top of code
 * Learn more at Quail Specification Chapter III
 * @author Tapeline
 */
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
    public String applyDirective(String code, File scriptHome,
                                 StringBoundariesMap boundaries, Object... arguments) {
        try {
            File targetFile = Paths.get(scriptHome.getAbsolutePath(), ((String) arguments[0])).toFile();
            String contents = FileUtils.readFileToString(
                    targetFile,
                    GlobalFlags.encoding
            ).replace("\r\n", "\n");
            return "#Included from " + arguments[0] + "\n" + contents + "\n\n" + code;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
