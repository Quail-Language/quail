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

public class ParserPartialExpressionTests {

    @Test
    public void testAssign() throws LexerException, ParserException {
        String code = "" +
                "a = 3\n" +
                "b = a\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[assign{a 3.0} assign{b a}]",
                node.stringRepr()
        );
    }

    @Test
    public void testBinary() throws LexerException, ParserException {
        String code = "" +
                "b / 4\n" +
                "c + 2\n" +
                "a % 3\n" +
                "d // 2\n" +
                "e >> d\n" +
                "e > d\n" +
                "d << e\n" +
                "d < e\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "op{b DIVIDE 4.0} " +
                        "op{c PLUS 2.0} " +
                        "op{a MODULO 3.0} " +
                        "op{d INTDIV 2.0} " +
                        "op{e SHIFT_RIGHT d} " +
                        "op{e GREATER d} " +
                        "op{d SHIFT_LEFT e} " +
                        "op{d LESS e}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testCall() throws LexerException, ParserException {
        String code = "" +
                "f()\n" +
                "f(2)\n" +
                "f(2, 3)\n" +
                "f(f())\n" +
                "f(f(), f(2))\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "call{f []} " +
                        "call{f [2.0]} " +
                        "call{f [2.0 3.0]} " +
                        "call{f [call{f []}]} " +
                        "call{f [call{f []} call{f [2.0]}]}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testFieldReference() throws LexerException, ParserException {
        String code = "" +
                "i.f\n" +
                "i.f()\n" +
                "f().f\n" +
                "f().f()\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "field{i f} " +
                        "call{field{i f} []} " +
                        "field{call{f []} f} " +
                        "call{field{call{f []} f} []}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testFieldSet() throws LexerException, ParserException {
        String code = "" +
                "i.f = v\n" +
                "f().f = v\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "field set{i f v} " +
                        "field set{call{f []} f v}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testIndexing() throws LexerException, ParserException {
        String code = "" +
                "i[f]\n" +
                "i[f]()\n" +
                "f()[f]\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "index{i f} " +
                        "call{index{i f} []} " +
                        "index{call{f []} f}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testIndexSet() throws LexerException, ParserException {
        String code = "" +
                "i[f] = v\n" +
                "f()[f] = v\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "index set{i f v} " +
                        "index set{call{f []} f v}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testSubscript() throws LexerException, ParserException {
        String code = "" +
                "s[::]\n" +
                "s[:x]\n" +
                "s[:x:y]\n" +
                "s[::x]\n" +
                "s[x:y]\n" +
                "s[x::y]\n" +
                "s[x:y:z]\n" +
                "s[x:+y]\n" +
                "s[x:+y:z]\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "sub{s null null null} " +
                        "sub{s null x null} " +
                        "sub{s null x y} " +
                        "sub{s null null x} " +
                        "sub{s x y null} " +
                        "sub{s x null y} " +
                        "sub{s x y z} " +
                        "sub{s x y null} " +
                        "sub{s x y z}" +
                        "]",
                node.stringRepr()
        );
    }

    @Test
    public void testTypecast() throws LexerException, ParserException {
        String code = "" +
                "string(3)\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[cast{512 3.0}]",
                node.stringRepr()
        );
    }

    @Test
    public void testUnary() throws LexerException, ParserException {
        String code = "" +
                "-3\n" +
                "!false\n" +
                "not false\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[op{MINUS 3.0} op{NOT false} op{NOT false}]",
                node.stringRepr()
        );
    }

    @Test
    public void testArrayAndMatrix() throws LexerException, ParserException {
        String code = "" +
                "[-] [3, 4, 5]\n" +
                "{!} [[false], [true]]\n" +
                "[1, 2] [+] [3, 4]\n" +
                "[[1], [2]] {/} [[3], [4]]\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[op{MINUS list[3.0 4.0 5.0]} " +
                        "op{NOT list[list[false] list[true]]} " +
                        "op{list[1.0 2.0] PLUS list[3.0 4.0]} " +
                        "op{list[list[1.0] list[2.0]] DIVIDE list[list[3.0] list[4.0]]}]",
                node.stringRepr()
        );
    }

}
