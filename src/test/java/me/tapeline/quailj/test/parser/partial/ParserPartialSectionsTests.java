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

public class ParserPartialSectionsTests {

    @Test
    public void testFor() throws LexerException, ParserException {
        String code = "" +
                "for i in c print(i)\n" +
                "for k, v in d print(k, v)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "for{[i] c call{print [i]}} " +
                        "for{[k, v] d call{print [k v]}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testIf() throws LexerException, ParserException {
        String code = "" +
                "if condition print(1)";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "if{[condition] [block[call{print [1.0]}]] null}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testLoopStop() throws LexerException, ParserException {
        String code = "" +
                "loop\n" +
                "print(1)\n" +
                "stop when c\n" +
                "\n" +
                "loop {\n" +
                "print(1)\n" +
                "} stop when c\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "loop{c block[call{print [1.0]}]} " +
                        "loop{c block[call{print [1.0]}]}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testThrough() throws LexerException, ParserException {
        String code = "" +
                "through 1:10 as i print(i)\n" +
                "through 1:10:1 as i print(i)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "through{1.0 10.0 null i call{print [i]}} " +
                        "through{1.0 10.0 1.0 i call{print [i]}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testTry() throws LexerException, ParserException {
        String code = "" +
                "try {\n" +
                "assert a\n" +
                "} catch AE as e {\n" +
                "assert b\n" +
                "}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "try{block[assert{a}] [catch{AE e block[assert{b}]}]}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testWhile() throws LexerException, ParserException {
        String code = "" +
                "while c print(1)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "while{c call{print [1.0]}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testIndentationBlock1() throws LexerException, ParserException {
        String code = "" +
                "while c:\n" +
                "\tprint(1)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "while{c block[call{print [1.0]}]}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testIndentationBlock2() throws LexerException, ParserException {
        String code = "" +
                "while c:\n" +
                "\twhile c:\n" +
                "\t\tprint(1)\n" +
                "\tprint(1)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "while{c block[while{c block[call{print [1.0]}]} call{print [1.0]}]}" +
                        "]",
                node.stringRepr()
        );
    }

}
