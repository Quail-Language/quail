package me.tapeline.quail.qdk;

import me.tapeline.quail.qdk.templater.Templater;
import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, LexerException, ParserException {

        if (args.length == 0) printUsage();

        if (args[0].equalsIgnoreCase("templateBuiltIns")) {
            if (args.length != 5) printUsage();

            String code = FileUtils.readFileToString(new File(args[1]), "UTF-8");
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.scan();
            Parser parser = new Parser(code, tokens);
            BlockNode nodes = parser.parse();
            Templater templater = new Templater(nodes.nodes, args[2], args[3]);
            templater.templateAndWrite(args[4]);
        }

    }

    public static void printUsage() {
        System.out.println("Usage: ");
        System.out.println("    templateBuiltIns <quail file.q> <package> <prefix> <target folder>");
        System.out.println("    | Creates templates for Java-implemented functions \n" +
                           "    | based on given quail file");
        System.exit(0);
    }

}