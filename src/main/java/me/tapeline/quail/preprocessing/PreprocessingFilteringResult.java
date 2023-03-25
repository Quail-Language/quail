package me.tapeline.quail.preprocessing;

import java.util.ArrayList;
import java.util.List;

public class PreprocessingFilteringResult {

    private List<String> directives = new ArrayList<>();
    private String filteredCode;

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
