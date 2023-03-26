package me.tapeline.quail;

import me.tapeline.quail.lexing.Lexer;
import me.tapeline.quail.lexing.LexerException;
import me.tapeline.quail.lexing.Token;

import java.util.List;

public class Main {

    public static void main(String[] args) throws LexerException {
        Lexer lexer = new Lexer("" +
                "print(\"Hello, World\")\n" +
                "a = 2\n" +
                "for b in 1:30:\n" +
                "\ta //= b\n" +
                "    print(b)\n" +
                "assert a == 2\n" +
                "use \"lang/math\" = math\n" +
                "function fact(x) math.product(1:+x)\n" +
                "print(fact(5))"
        );
        List<Token> tokens = lexer.scan();
        StringBuilder builder = new StringBuilder();
        for (Token token : tokens)
            builder.append(token.toString()).append(" ");
        System.out.println(builder.toString());
    }

}
