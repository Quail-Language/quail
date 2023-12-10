package me.tapeline.quailj.test.parser.partial;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserPartialEffectTests {

    @Test
    public void testAsync() throws LexerException, ParserException {
        String code = "" +
                "async 3\n" +
                "async print()\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[async{3.0} async{call{print []}}]",
                node.stringRepr()
        );
    }

    @Test
    public void testEffects() throws LexerException, ParserException {
        String code = "" +
                "throw \"ERROR!\"\n" +
                "strike 3\n" +
                "assert 4 == 4\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[throw{\"ERROR!\"} strike{3.0} assert{op{4.0 EQUALS 4.0}}]",
                node.stringRepr()
        );
    }

    @Test
    public void testReturn() throws LexerException, ParserException {
        String code = "" +
                "return calc(3, 4)\n" +
                "return\n" +
                "return 3\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[return{call{calc [3.0 4.0]}} return{null} return{3.0}]",
                node.stringRepr()
        );
    }

    @Test
    public void testInstructions() throws LexerException, ParserException {
        String code = "" +
                "break\n" +
                "continue\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[break continue]",
                node.stringRepr()
        );
    }

    @Test
    public void testUse() throws LexerException, ParserException {
        String code = "" +
                "use \"lang/math\" = math\n" +
                "use \"lang/\\\"\" = quote\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[use{lang/math math} use{lang/\" quote}]",
                node.stringRepr()
        );
    }

}
