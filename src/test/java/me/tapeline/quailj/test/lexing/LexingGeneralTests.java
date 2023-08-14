package me.tapeline.quailj.test.lexing;

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
        String expected = "VAR print LPAR ( LITERAL_STR Hello, World RPAR " +
                ") EOL VAR a ASSIGN = LITERAL_NUM 2 EOL CONTROL_FOR for VAR b " +
                "IN in LITERAL_NUM 1 RANGE : LITERAL_NUM 30 RANGE : EOL TAB " +
                "\t VAR a SHORT_INTDIV //= VAR b EOL TAB      VAR print LPAR ( " +
                "VAR b RPAR ) EOL EFFECT_ASSERT assert VAR a EQUALS == LITERAL_" +
                "NUM 2 EOL EFFECT_USE use LITERAL_STR lang/math ASSIGN = " +
                "VAR math EOL TYPE_FUNCTION function VAR fact LPAR ( VAR x RPAR " +
                ") VAR math DOT . VAR product LPAR ( LITERAL_NUM 1 RANGE_INCLUD" +
                "E :+ VAR x RPAR ) EOL VAR print LPAR ( VAR fact LPAR ( LITERAL_" +
                "NUM 5 RPAR ) RPAR ) ";
        Assertions.assertArrayEquals(
                expected.toCharArray(),
                builder.toString().toCharArray()
        );
    }

    @Test
    public void testQuoteInString() throws LexerException {
        Lexer lexer = new Lexer("a = \"\\\"\"");
        List<Token> tokens = lexer.scan();
        StringBuilder builder = new StringBuilder();
        for (Token token : tokens)
            builder.append(token.toString()).append(" ");
        System.out.println(builder);
        String expected = "VAR a ASSIGN = LITERAL_STR \" ";
        Assertions.assertArrayEquals(
                expected.toCharArray(),
                builder.toString().toCharArray()
        );
    }

}
