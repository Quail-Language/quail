package preprocessing;

import me.tapeline.quailj.preprocessing.Preprocessor;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.preprocessing.directives.AliasDirective;
import me.tapeline.quailj.preprocessing.directives.IncludeDirective;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PreprocessingIncludeTests {

    @BeforeAll
    public static void prepare() {
        Preprocessor.registeredDirectives.clear();
        Preprocessor.registeredDirectives.add(new AliasDirective());
        Preprocessor.registeredDirectives.add(new IncludeDirective());
    }

    @Test
    public void basicInclude() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:include \"testAssets/include.q\"\n"
        );
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "#Included from testAssets/include.q\n" +
                        "print(\"File included\")" +
                        "\n\n",
                result
        );
    }

    @Test
    public void directivesContainingInclude() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:include \"testAssets/directives.q\"\n" +
                "print(\"Another hello\")"
        );
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "#Included from testAssets/directives.q\n" +
                        "a = 2\n" +
                        "c = 455.3\n" +
                        "\n" +
                        "print(\"Another hello\")",
                result
        );
    }

}
