package parser.complex;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserComplexExpressionTests {

    @Test
    public void test1() throws LexerException, ParserException {
        String code = "" +
                "a = a + b / x << z % (c [+] d)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[assign{a op{op{a PLUS op{b DIVIDE x}} SHIFT_LEFT op{z MODULO op{c PLUS d}}}}]",
                node.stringRepr()
        );
    }

    @Test
    public void test2() throws LexerException, ParserException {
        String code = "" +
                "f().d[e](c)[f].f = a + b / x << z % (c [+] d)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[field set{" +
                        "index{call{index{field{call{f []} d} e} [c]} f} " +
                        "f " +
                        "op{op{a PLUS op{b DIVIDE x}} SHIFT_LEFT op{z MODULO op{c PLUS d}}}}]",
                node.stringRepr()
        );
    }

}
