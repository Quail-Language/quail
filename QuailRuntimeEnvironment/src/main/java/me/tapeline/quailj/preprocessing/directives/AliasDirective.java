package me.tapeline.quailj.preprocessing.directives;

import me.tapeline.quailj.preprocessing.StringBoundariesMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aliasing directive. Replaces all regex occurrences
 * in code according to given arguments. Omits occurrences
 * in strings.
 * Learn more at Quail Specification Chapter III
 * @author Tapeline
 */
public class AliasDirective extends AbstractDirective {

    @Override
    public String prefix() {
        return "alias";
    }

    @Override
    public List<DirectiveArgument> arguments() {
        return Arrays.asList(DirectiveArgument.STRING, DirectiveArgument.CODE);
    }

    @Override
    public String applyDirective(String code, File scriptHome,
                                 StringBoundariesMap boundaries, Object... arguments) {
        Pattern pattern = Pattern.compile(((String) arguments[0]), Pattern.MULTILINE);
        StringBuilder part = new StringBuilder();
        List<String> parts = new ArrayList<>();
        List<Boolean> substitute = new ArrayList<>();
        boolean inString = false;
        for (int i = 0; i < code.length(); i++) {
            if (!inString && boundaries.isStringBeginning(i)) {
                parts.add(part.toString());
                substitute.add(true);
                inString = true;
                part = new StringBuilder(String.valueOf(code.charAt(i)));
            } else if (inString && boundaries.isStringEnding(i)) {
                part.append(code.charAt(i));
                parts.add(part.toString());
                substitute.add(false);
                inString = false;
                part = new StringBuilder();
            } else {
                part.append(code.charAt(i));
            }
        }
        parts.add(part.toString());
        substitute.add(true);

        StringBuilder substitutedCode = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            if (substitute.get(i)) {
                Matcher matcher = pattern.matcher(parts.get(i));
                substitutedCode.append(matcher.replaceAll(((String) arguments[1])));
            } else {
                substitutedCode.append(parts.get(i));
            }
        }

        return substitutedCode.toString();
    }

}
