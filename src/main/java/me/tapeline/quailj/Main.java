package me.tapeline.quailj;

import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class Main {

    public static void main(String[] args) throws LexerException, ParserException {
        String code = "" +
        "print(\"Hello, World\")\n" +
        "a = 2\n" +
        "for b in 1:30:\n" +
        "\ta //= b\n" +
        "    print(b)\n" +
        "assert a == 2\n" +
        "use \"lang/math\" = math\n" +
        "function fact(x) math.product(1:+x)\n" +
        "print(fact(5))\n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.scan();
        StringBuilder builder = new StringBuilder();
        for (Token token : tokens)
            builder.append(token.toString()).append(" ");
        System.out.println(builder.toString());
        Parser parser = new Parser(code, tokens);
        Node node = parser.parse();
        System.out.println("\n" + node.stringRepr());
    }

}
