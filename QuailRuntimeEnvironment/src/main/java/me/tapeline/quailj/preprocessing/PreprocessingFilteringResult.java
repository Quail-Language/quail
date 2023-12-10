package me.tapeline.quailj.preprocessing;

import java.util.List;

public class PreprocessingFilteringResult {

    private final List<String> directives;
    private final String filteredCode;

    public PreprocessingFilteringResult(List<String> directives, String filteredCode) {
        this.directives = directives;
        this.filteredCode = filteredCode;
    }

    public List<String> getDirectives() {
        return directives;
    }

    public String getFilteredCode() {
        return filteredCode;
    }

}
