package me.tapeline.quailj.preprocessing;

import me.tapeline.quailj.addons.QuailAddon;
import me.tapeline.quailj.addons.QuailAddonRegistry;
import me.tapeline.quailj.preprocessing.directives.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Preprocessor {
    public static final List<AbstractDirective> registeredDirectives = new ArrayList<>();

    public static void resetDirectives() {
        registeredDirectives.clear();
        registeredDirectives.add(new IncludeDirective());
        registeredDirectives.add(new AliasDirective());
        for (QuailAddon addon : QuailAddonRegistry.getAddons())
            registeredDirectives.addAll(addon.providedDirectives());
    }

    private int pos;
    private String code;
    private final File scriptHome;

    public Preprocessor(String code, File scriptHome) {
        this.code = code;
        this.scriptHome = scriptHome;
    }

    private boolean checkPrefix() {
        return code.substring(pos).startsWith("#:");
    }

    private boolean reachedEnd() {
        return pos >= code.length();
    }

    private char peek() {
        if (reachedEnd()) return '\0';
        return code.charAt(pos);
    }

    private char next() {
        return code.charAt(pos++);
    }

    private String consumeString() throws PreprocessorException {
        boolean escaped = false;
        StringBuilder str = new StringBuilder();
        while (!(reachedEnd() || (!escaped && peek() == '"'))) {
            if (!escaped && peek() == '\\') {
                escaped = true;
                next();
            } else {
                escaped = false;
                str.append(next());
            }
        }
        if (reachedEnd()) {
            throw new PreprocessorException("Unexpected string end");
        }
        next();
        return str.toString();
    }

    private String consumeArgument() throws PreprocessorException {
        while (peek() == ' ') next();
        if (peek() == '"') {
            next();
            return consumeString();
        } else {
            int start = pos;
            while (!(reachedEnd() || peek() == ' '))
                next();
            return code.substring(start, pos);
        }
    }

    private Object getArgument(DirectiveArgument argument) throws PreprocessorException {
        while (peek() == ' ') next();
        if (argument == DirectiveArgument.CODE) {
            return code.substring(pos);
        }
        String argumentRepr = consumeArgument();
        String clearedArg = argumentRepr
                .replace("\n", "")
                .replace("\t", "")
                .replace(" ", "");
        switch (argument) {
            case INT:
                return Integer.parseInt(clearedArg);
            case BOOL:
                return clearedArg.equalsIgnoreCase("true") ||
                        clearedArg.equalsIgnoreCase("1") ||
                        clearedArg.equalsIgnoreCase("enable");
            case STRING:
                return argumentRepr;
        }
        return null;
    }

    private void resetCaret() {
        pos = 0;
    }

    private PreprocessingFilteringResult filterDirectives(String code) {
        String[] lines = code.split("\n");
        List<String> directives = new ArrayList<>();
        StringBuilder filteredCode = new StringBuilder();
        for (int i = 0; i < lines.length; ) {
            if (lines[i].trim().startsWith("#:") &&
                lines[i].trim().endsWith("\\")) {
                String line = lines[i].trim();
                StringBuilder directive = new StringBuilder();
                while (line.endsWith("\\")) {
                    line = lines[i++].trim();
                    if (line.endsWith("\\"))
                        directive.append(line, 0, line.length() - 1).append("\n");
                    else
                        directive.append(line);
                }
                directives.add(directive.toString());
            } else if (lines[i].trim().startsWith("#:")) {
                directives.add(lines[i].trim());
                i++;
            } else {
                filteredCode.append(lines[i]);
                if (i + 1 < lines.length) filteredCode.append("\n");
                i++;
            }
        }
        return new PreprocessingFilteringResult(directives, filteredCode.toString());
    }

    private StringBoundariesMap resolveStringBoundaries(String code) {
        int pos = 0;
        StringBoundariesMap boundaries = new StringBoundariesMap();
        while (pos < code.length()) {
            if (code.charAt(pos++) == '"') {
                int left = pos - 1;
                boolean escaped = false;
                StringBuilder str = new StringBuilder();
                while (!(pos >= code.length() || (!escaped && code.charAt(pos) == '"'))) {
                    if (!escaped && code.charAt(pos) == '\\') {
                        escaped = true;
                        pos++;
                    } else {
                        escaped = false;
                        str.append(code.charAt(pos++));
                    }
                }
                pos++;
                boundaries.addBoundary(left, pos - left);
            }
        }
        return boundaries;
    }

    private List<ScannedDirective> parseDirectives(List<String> preparedDirectives)
            throws PreprocessorException {
        List<ScannedDirective> directives = new ArrayList<>();
        for (String preparedDirective : preparedDirectives) {
            for (AbstractDirective directive : registeredDirectives) {
                if (preparedDirective.startsWith("#:" + directive.prefix())) {
                    code = preparedDirective.substring(directive.prefix().length() + 2);
                    resetCaret();
                    List<Object> arguments = new ArrayList<>();
                    for (DirectiveArgument argument : directive.arguments())
                        arguments.add(getArgument(argument));
                    directives.add(new ScannedDirective(arguments.toArray()) {
                        @Override
                        public AbstractDirective directive() {
                            return directive;
                        }
                    });
                    break;
                }
            }
        }
        return directives;
    }

    private String executeDirectives(List<ScannedDirective> directives, String code,
                                     StringBoundariesMap boundaries) {
        for (ScannedDirective directive : directives) {
            code = directive.apply(code, scriptHome, boundaries);
        }
        return code;
    }

    public String preprocess() throws PreprocessorException {
        String code = this.code;
        while (true) {
            PreprocessingFilteringResult result = filterDirectives(code);
            if (result.getDirectives().isEmpty()) break;
            code = result.getFilteredCode();
            StringBoundariesMap boundaries = resolveStringBoundaries(result.getFilteredCode());
            List<ScannedDirective> directives = parseDirectives(result.getDirectives());
            code = executeDirectives(directives, code, boundaries);
        }
        return code;
    }

}
