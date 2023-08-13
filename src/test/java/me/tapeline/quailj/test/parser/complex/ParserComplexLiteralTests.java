package me.tapeline.quailj.test.parser.complex;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserComplexLiteralTests {

    @Test
    public void testComplexLiteral() throws LexerException, ParserException {
        String code = "" +
                "class Test like Test2 {\n" +
                "   num n\n" +
                "   v = 2\n" +
                "   d = {a=false, b=[2, 4, 6]}\n" +
                "   l1 = [\"\\\"bbb\", 1]\n" +
                "   f2 = (a, b) -> {return null}\n" +
                "   method f() {}\n" +
                "}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "class{Test Test2 [" +
                        "assignvar{n 0.0} " +
                        "assignvar{v 2.0} " +
                        "assignvar{d dict{[a b] [false list[2.0 4.0 6.0]]}} " +
                        "assignvar{l1 list[\"\"bbb\" 1.0]} " +
                        "assignvar{f2 lambda{[a=null b=null] block[return{null}]}}] " +
                        "{f=function{f [] block[]}} " +
                        "[]" +
                        "}" +
                        "]",
                node.stringRepr()
        );
    }

}
