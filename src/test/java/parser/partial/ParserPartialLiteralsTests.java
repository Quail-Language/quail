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

public class ParserPartialLiteralsTests {

    @Test
    public void testBool() throws LexerException, ParserException {
        String code = "" +
                "false\n" +
                "true\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[false true]",
                node.stringRepr()
        );
    }

    @Test
    public void testClass() throws LexerException, ParserException {
        String code = "" +
                "class Test {\n" +
                "   num n\n" +
                "   v = 2\n" +
                "   method f() {}\n" +
                "}\n" +
                "class Test like Test2 {\n" +
                "   num n\n" +
                "   v = 2\n" +
                "   method f() {}\n" +
                "}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "class{Test null {n=0.0 v=2.0} {f=function{f [] block[]}} []} " +
                        "class{Test Test2 {n=0.0 v=2.0} {f=function{f [] block[]}} []}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testDict() throws LexerException, ParserException {
        String code = "" +
                "a = {k1=v1, k2=v2}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "assign{a dict{[k1 k2] [v1 v2]}}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testFunction() throws LexerException, ParserException {
        String code = "" +
                "function f() {}\n" +
                "function f() return\n" +
                "method m() {}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "function{f [] block[]} " +
                        "function{f [] return{null}} " +
                        "function{m [] block[]}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testLambda() throws LexerException, ParserException {
        String code = "" +
                "(a, b) -> {return null}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "lambda{[a b] block[return{null}]}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testList() throws LexerException, ParserException {
        String code = "" +
                "[a, b, 2]\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "list[a b 2.0]" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testNull() throws LexerException, ParserException {
        String code = "" +
                "null\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "null" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testNum() throws LexerException, ParserException {
        String code = "" +
                "4.2\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "4.2" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testString() throws LexerException, ParserException {
        String code = "" +
                "\"a\"\n" +
                "\"\\\"q\"";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "\"a\" \"\"q\"" +
                        "]",
                node.stringRepr()
        );
    }

}
