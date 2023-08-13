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

public class ParserComplexSectionsTests {

    @Test
    public void testComplexSections() throws LexerException, ParserException {
        String code = "" +
                "while True:\n" +
                "    if c1 then\n" +
                "        try {\n" +
                "            for i in 0:10 f()\n" +
                "        } catch AE as e {\n" +
                "            loop\n" +
                "                print(e.next())\n" +
                "            stop when e.ended()\n" +
                "            through 0:10 as i:\n" +
                "                print(i)\n" +
                "        }\n" +
                "    else {\n" +
                "        print(f)\n" +
                "    }";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "while{True block[" +
                        "if{[c1] [block[" +
                        "try{block[" +
                        "for{[i] range{0.0 10.0 null} call{f []}}" +
                        "] [catch{AE e block[" +
                        "loop{call{field{e ended} []} block[" +
                        "call{print [call{field{e next} []}]}" +
                        "]} " +
                        "through{0.0 10.0 null i block[" +
                        "call{print [i]}" +
                        "]}" +
                        "]}" +
                        "]}" +
                        "]] null}]} block[call{print [f]}]]",
                node.stringRepr()
        );
    }

}
/*
while True:
    if c1 then
        try {
            for i in 0:10 f()
        } catch AE as e {
            loop
                print(e.next())
            stop when e.ended()
            through 0:10 as i:
                print(i)
        }
    else {
        print(f)
    }

block[
while{True block[
if{[c1] [block[
try{block[
for{[i] range{0.0 10.0 null} call{f []}}
] [catch{AE e block[
loop{call{field{e ended} []} block[
call{print [call{field{e next} []}]}
]}
through{0.0 10.0 null i block[
call{print [i]}
]}
]}
]}
]] null}]} block[call{print [f]}]]
 */