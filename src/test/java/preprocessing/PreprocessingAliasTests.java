package preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import me.tapeline.quailj.preprocessing.Preprocessor;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.preprocessing.directives.AliasDirective;

import java.io.File;

public class PreprocessingAliasTests {

    @BeforeAll
    public static void prepare() {
        Preprocessor.registeredDirectives.clear();
        Preprocessor.registeredDirectives.add(new AliasDirective());
    }

    @Test
    public void basicSubstitution() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:alias \"hello\" print(\"Hello, World!\")\n" +
                "hello",
        new File(""));
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "print(\"Hello, World!\")",
                result
        );
    }

    @Test
    public void basicSubstitutionWithStrings() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:alias \"hello\" print(\"Hello, World!\")\n" +
                "hello\n" +
                "print(\"Another hello\")",
        new File(""));
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "print(\"Hello, World!\")\n" +
                        "print(\"Another hello\")",
                result
        );
    }

    @Test
    public void groupSubstitution() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:alias \"set\\\\s*(?<id>[a-zA-Z0-9.]+)\\\\s*to\\\\s*(?<value>.*)\" $1 = $2\n" +
                "set a to 2",
        new File(""));
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "a = 2",
                result
        );
    }

    @Test
    public void groupSubstitutionWithStrings() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:alias \"set\\\\s*(?<id>[a-zA-Z0-9.]+)\\\\s*to\\\\s*(?<value>.*)\" $1 = $2\n" +
                "set a to 2\n" +
                "b = \"set something to something\"",
        new File(""));
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "a = 2\n" +
                        "b = \"set something to something\"",
                result
        );
    }

}
