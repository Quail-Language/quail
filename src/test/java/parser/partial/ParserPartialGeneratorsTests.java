package parser.partial;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserPartialGeneratorsTests {

    @Test
    public void testDictFor() throws LexerException, ParserException {
        String code = "" +
                "a = {k = v for k, v in dct}\n" +
                "a = {k = v for k, v in dct if k != e}";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "assign{a gendict{k v [k v] dct null}} " +
                        "assign{a gendict{k v [k v] dct op{k NOT_EQUALS e}}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testDictThrough() throws LexerException, ParserException {
        String code = "" +
                "a = {k = v through 0:10 as i}\n" +
                "a = {k = v through 0:10 as i if i != 2}";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "assign{a gendict{k v i range{0.0 10.0 null} null}} " +
                        "assign{a gendict{k v i range{0.0 10.0 null} op{i NOT_EQUALS 2.0}}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testListFor() throws LexerException, ParserException {
        String code = "" +
                "[v for v in lst]\n" +
                "[v for v in lst if v != e]";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "genlist{v [v] lst null} " +
                        "genlist{v [v] lst op{v NOT_EQUALS e}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testListThrough() throws LexerException, ParserException {
        String code = "" +
                "[v through 0:10 as i]\n" +
                "[v through 0:10 as i if i != 2]";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "genlist{v i range{0.0 10.0 null} null} " +
                        "genlist{v i range{0.0 10.0 null} op{i NOT_EQUALS 2.0}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testRange() throws LexerException, ParserException {
        String code = "" +
                "0:10\n" +
                "0:10:2\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "range{0.0 10.0 null} " +
                        "range{0.0 10.0 2.0}" +
                        "]",
                node.stringRepr()
        );
    }

}
