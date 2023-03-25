package me.tapeline.quail;

import me.tapeline.quail.preprocessing.Preprocessor;
import me.tapeline.quail.preprocessing.PreprocessorException;
import me.tapeline.quail.preprocessing.directives.AliasDirective;

public class Main {

    public static void main(String[] args) throws PreprocessorException {
        Preprocessor.registeredDirectives.add(new AliasDirective());

        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:alias \"set\\\\s*(?<id>[a-zA-Z0-9.]+)\\\\s*to\\\\s*(?<value>.*)\" $1 = $2\n" +
                "set a to 2\n" +
                "b = \"set something to something\""
        );
        result = preprocessor.preprocess();
        System.out.println(result);
    }

}
