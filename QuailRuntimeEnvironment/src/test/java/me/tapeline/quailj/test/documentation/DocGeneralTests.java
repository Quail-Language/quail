package me.tapeline.quailj.test.documentation;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DocGeneralTests {

    @Test
    public void test1() throws LexerException, ParserException {
        String code = "" +
                "class Dict like Object {\n" +
                "    #? A string key -> object value data structure\n" +
                "\n" +
                "    list keys(this) {\n" +
                "        #? List all keys in this dict\n" +
                "    }\n" +
                "\n" +
                "    list values(this) {\n" +
                "        #? List all values in this dict\n" +
                "    }\n" +
                "\n" +
                "    list pairs(this) {\n" +
                "        #? List all <code>[key, value]</code> pairs\n" +
                "    }\n" +
                "\n" +
                "    bool containsKey(this, string key) {\n" +
                "        #? Check if this dict contains given key\n" +
                "    }\n" +
                "\n" +
                "    num size(this) {\n" +
                "        #? Returns size of this dict\n" +
                "    }\n" +
                "\n" +
                "    void set(this, string key, value) {\n" +
                "        #? Set given key to given value in this dict\n" +
                "    }\n" +
                "\n" +
                "    object get(this, string key) {\n" +
                "        #? Get value by given key in this dict\n" +
                "    }\n" +
                "\n" +
                "    static dict assemblePairs(list pairs) {\n" +
                "        #? Assemble dict from given list of <code>[key, value]</code> pairs\n" +
                "    }\n" +
                "\n" +
                "}";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
    }

}
