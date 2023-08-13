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

public class ParserComplexGeneratorsTests {

    @Test
    public void testComplexGenerator() throws LexerException, ParserException {
        String code = "" +
                "a = {k = [v for v in lst] for k, v in zip(map(str, 0:100), 0:100)}";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
        Assertions.assertEquals(
                "block[" +
                        "assign{a gendict{k genlist{v [v] lst null} [k, v] call{zip [call{map [str range{0.0 100.0 null}]} range{0.0 100.0 null}]} null}}" +
                        "]",
                node.stringRepr()
        );
    }

}
