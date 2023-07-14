package me.tapeline.quailj.launcher;

import me.tapeline.quailj.GlobalFlags;
import me.tapeline.quailj.io.DefaultIO;
import me.tapeline.quailj.lexing.Lexer;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import me.tapeline.quailj.preprocessing.Preprocessor;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class QuailLauncher {

    private HashMap<String, Object> localFlags;

    private void setupGlobalFlags(HashMap<String, Object> flags) {
        if (flags.containsKey("encoding"))
            GlobalFlags.encoding = flags.get("encoding").toString();
    }

    public QObject launch(String[] args) throws IOException, PreprocessorException,
            LexerException, ParserException, LauncherException {
        LaunchCommandParser launchCommandParser = new LaunchCommandParser(args);
        launchCommandParser.parseReceivedArgs();
        localFlags = launchCommandParser.getUserFlags();
        setupGlobalFlags(launchCommandParser.getGlobalFlags());
        String mode = launchCommandParser.getSelectedRunStrategy();

        if (!mode.equalsIgnoreCase("run") && !mode.equalsIgnoreCase("profile"))
            throw new LauncherException("Invalid mode " + mode);
        boolean doProfile = mode.equalsIgnoreCase("profile");

        String code = FileUtils.readFileToString(new File(launchCommandParser.getTargetScript()),
                GlobalFlags.encoding);
        File scriptHome = new File(launchCommandParser.getTargetScript()).getAbsoluteFile().getParentFile();

        Preprocessor preprocessor = new Preprocessor(code, scriptHome);
        String preprocessedCode = preprocessor.preprocess();

        Lexer lexer = new Lexer(preprocessedCode);
        List<Token> tokens = lexer.scan();

        Parser parser = new Parser(preprocessedCode, tokens);
        BlockNode parsedCode = parser.parse();

        Runtime runtime = new Runtime(parsedCode, preprocessedCode, scriptHome, new DefaultIO(), doProfile);
        QObject returnValue = QObject.Val(0);
        try {
            runtime.run(parsedCode, runtime.getMemory());
        } catch (RuntimeStriker striker) {
            if (striker.getType() == RuntimeStriker.Type.RETURN)
                returnValue = striker.getCarryingReturnValue();
            else if (striker.getType() == RuntimeStriker.Type.EXCEPTION) {
                System.err.println("Runtime error:");
                System.err.println(striker.formatError(preprocessedCode));
            } else if (striker.getType() == RuntimeStriker.Type.EXIT)
                returnValue = QObject.Val(striker.getExitCode());
        }
        return returnValue;
    }

    public QObject launchWithAnonymousCode(String code, File scriptHome, String[] args)
            throws LauncherException, PreprocessorException, LexerException, ParserException {
        LaunchCommandParser launchCommandParser = new LaunchCommandParser(args);
        launchCommandParser.parseReceivedArgs();
        localFlags = launchCommandParser.getUserFlags();
        setupGlobalFlags(launchCommandParser.getGlobalFlags());
        String mode = launchCommandParser.getSelectedRunStrategy();

        if (!mode.equalsIgnoreCase("run") && !mode.equalsIgnoreCase("profile"))
            throw new LauncherException("Invalid mode " + mode);
        boolean doProfile = mode.equalsIgnoreCase("profile");

        Preprocessor preprocessor = new Preprocessor(code, scriptHome);
        String preprocessedCode = preprocessor.preprocess();

        Lexer lexer = new Lexer(preprocessedCode);
        List<Token> tokens = lexer.scan();

        Parser parser = new Parser(preprocessedCode, tokens);
        BlockNode parsedCode = parser.parse();

        Runtime runtime = new Runtime(parsedCode, preprocessedCode, scriptHome, new DefaultIO(), doProfile);
        QObject returnValue = QObject.Val(0);
        try {
            runtime.run(parsedCode, runtime.getMemory());
        } catch (RuntimeStriker striker) {
            if (striker.getType() == RuntimeStriker.Type.RETURN)
                returnValue = striker.getCarryingReturnValue();
            else if (striker.getType() == RuntimeStriker.Type.EXCEPTION) {
                System.err.println("Runtime error:");
                System.err.println(striker.formatError(preprocessedCode));
            } else if (striker.getType() == RuntimeStriker.Type.EXIT)
                returnValue = QObject.Val(striker.getExitCode());
        }
        return returnValue;
    }

    public QObject launchAndHandleErrors(String[] args) {
        try {
            return launch(args);
        } catch (PreprocessorException e) {
            System.err.println("Preprocessor error:");
            System.err.println(e.getMessage());
        } catch (ParserException e) {
            System.err.println("Parser error:");
            System.err.println(e.formatError());
        } catch (LauncherException e) {
            System.err.println("Launcher error:");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("An unhandled exception occurred during script launch:");
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        } catch (LexerException e) {
            System.err.println("Lexer error:");
            System.err.println(e.formatError());
        }
        return null;
    }

    public QObject launchAnonymousAndHandleErrors(String code, File scriptHome, String[] args) {
        try {
            return launchWithAnonymousCode(code, scriptHome, args);
        } catch (PreprocessorException e) {
            System.err.println("Preprocessor error:");
            System.err.println(e.getMessage());
        } catch (ParserException e) {
            System.err.println("Parser error:");
            System.err.println(e.formatError());
        } catch (LauncherException e) {
            System.err.println("Launcher error:");
            System.err.println(e.getMessage());
        } catch (LexerException e) {
            System.err.println("Lexer error:");
            System.err.println(e.formatError());
        }
        return null;
    }

}
