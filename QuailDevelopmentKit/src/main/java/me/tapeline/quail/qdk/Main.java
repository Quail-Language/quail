package me.tapeline.quail.qdk;

import me.tapeline.quail.qdk.debugclient.DebugClient;
import me.tapeline.quail.qdk.debugclient.gui.DebuggerWindow;
import me.tapeline.quail.qdk.libconverter.Converter;
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
    public static void main(String[] args) throws
            IOException, LexerException, ParserException, ClassNotFoundException {

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
        } else if (args[0].equalsIgnoreCase("convert")) {
            if (args.length != 5) printUsage();

            Converter converter = new Converter(
                    args[1].split(";"),
                    new File(args[4]),
                    args[2],
                    args[3]
            );
            converter.convert();
        } else if (args[0].equalsIgnoreCase("debugClient")) {
            if (args.length < 3) printUsage();
            DebugClient client = new DebugClient(args[1], Short.parseShort(args[2]));
            client.run();
        } else if (args[0].equalsIgnoreCase("debugClientGui")) {
            DebuggerWindow.runGuiDebugger();
        }

    }

    public static void printUsage() {
        System.out.println("Usage: ");
        System.out.println("    templateBuiltIns <quail file.q> <package> <prefix> <target folder>");
        System.out.println("    | Creates templates for Java-implemented functions \n" +
                           "    | based on given quail file");
        System.out.println("    convert <com.pkg.C1;lib.jar:org.xyz.C2...> <package> <prefix> <target folder>");
        System.out.println("    | Creates templates for Java-implemented Quail classes and methods based\n" +
                           "    | on given Java classes");
        System.out.println("    debugClient <host> <port>");
        System.out.println("    | Start a QSDb client");
        System.exit(0);
    }

}