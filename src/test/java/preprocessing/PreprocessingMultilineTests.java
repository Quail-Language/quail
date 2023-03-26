package preprocessing;

import me.tapeline.quail.preprocessing.Preprocessor;
import me.tapeline.quail.preprocessing.PreprocessorException;
import me.tapeline.quail.preprocessing.StringBoundariesMap;
import me.tapeline.quail.preprocessing.directives.AbstractDirective;
import me.tapeline.quail.preprocessing.directives.AliasDirective;
import me.tapeline.quail.preprocessing.directives.DirectiveArgument;
import me.tapeline.quail.preprocessing.directives.IncludeDirective;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class PreprocessingMultilineTests {

    @BeforeAll
    public static void prepare() {
        Preprocessor.registeredDirectives.clear();
        Preprocessor.registeredDirectives.add(new AbstractDirective() {
            @Override
            public String prefix() {
                return "dummy";
            }

            @Override
            public List<DirectiveArgument> arguments() {
                return Arrays.asList(DirectiveArgument.INT, DirectiveArgument.INT);
            }

            @Override
            public String applyDirective(String code, StringBoundariesMap boundaries, Object... arguments) {
                return "" + arguments[0] + arguments[1] + code;
            }
        });
        Preprocessor.registeredDirectives.add(new AliasDirective());
    }

    @Test
    public void basicMultiline() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:dummy 5 \\\n" +
                "6"
        );
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "56",
                result
        );
    }

    @Test
    public void codeMultiline() throws PreprocessorException {
        Preprocessor preprocessor;
        String result;
        preprocessor = new Preprocessor("" +
                "#:alias \"twicehello\" print(\"Hello\") \\\n" +
                "print(\"Hello\")\n" +
                "twicehello"
        );
        result = preprocessor.preprocess();
        System.out.println(result);
        Assertions.assertEquals(
                "print(\"Hello\") \n" +
                        "print(\"Hello\")",
                result
        );
    }

}
