package lexing;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LexingGeneralTests {

    @Test
    public void test1() throws LexerException {
        Lexer lexer = new Lexer("" +
                "print(\"Hello, World\")\n" +
                "a = 2\n" +
                "for b in 1:30:\n" +
                "\ta //= b\n" +
                "    print(b)\n" +
                "assert a == 2\n" +
                "use \"lang/math\" = math\n" +
                "function fact(x) math.product(1:+x)\n" +
                "print(fact(5))"
        );
        List<Token> tokens = lexer.scan();
        StringBuilder builder = new StringBuilder();
        for (Token token : tokens)
            builder.append(token.toString()).append(" ");
        System.out.println(builder);
        String expected = "ID print LPAR ( LITERAL_STR \"Hello, World\" RPAR " +
                ") EOL ID a ASSIGN = LITERAL_NUM 2 EOL CONTROL_FOR for ID b " +
                "IN in LITERAL_NUM 1 RANGE : LITERAL_NUM 30 RANGE : EOL TAB " +
                "\t ID a SHORT_INTDIV //= ID b EOL TAB      ID print LPAR ( " +
                "ID b RPAR ) EOL EFFECT_ASSERT assert ID a EQUALS == LITERAL_" +
                "NUM 2 EOL EFFECT_USE use LITERAL_STR \"lang/math\" ASSIGN = " +
                "ID math EOL TYPE_FUNCTION function ID fact LPAR ( ID x RPAR " +
                ") ID math DOT . ID product LPAR ( LITERAL_NUM 1 RANGE_INCLUD" +
                "E :+ ID x RPAR ) EOL ID print LPAR ( ID fact LPAR ( LITERAL_" +
                "NUM 5 RPAR ) RPAR ) ";
        Assertions.assertArrayEquals(
                expected.toCharArray(),
                builder.toString().toCharArray()
        );
    }

}
