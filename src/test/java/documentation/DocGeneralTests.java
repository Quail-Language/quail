package documentation;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DocGeneralTests {

    @Test
    public void test1() throws LexerException, ParserException {
        String code = "" +
                "#?toc-entry Object" +
                "#?toc-entry Object::get" +
                "" +
                "class Object {\n" +
                "   #?author Tapeline\n" +
                "   #?badge Mutable\n" +
                "   #? Ancestor for all\n" +
                "\n" +
                "   object get(string key) {\n" +
                "       #? Gets table value by given key\n" +
                "   }\n" +
                "}\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println(node.stringRepr());
    }

}
