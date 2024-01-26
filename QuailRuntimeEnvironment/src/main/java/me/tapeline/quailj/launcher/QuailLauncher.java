package me.tapeline.quailj.launcher;

import me.tapeline.quailj.GlobalFlags;
import me.tapeline.quailj.debugging.DebugServer;
import me.tapeline.quailj.docgen.DocumentationGenerator;
import me.tapeline.quailj.io.DefaultIO;
import me.tapeline.quailj.io.IO;
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
// TODO: refactor
    public static final int QUAIL_MAJOR_VERSION = 2;
    public static final int QUAIL_MINOR_VERSION = 0;
    public static final int QUAIL_PATCH_VERSION = 0;
    public static final String QUAIL_VERSION_STATUS = "alpha";
    public static final int QUAIL_SUBVERSION = 7;
    public static final String QUAIL_VERSION_SUFFIX = "RC-2";

    private HashMap<String, Object> localFlags;

    private boolean parseBoolFlag(String flag) {
        return flag.equalsIgnoreCase("true") ||
                flag.equalsIgnoreCase("1") ||
                flag.equalsIgnoreCase("enable");
    }

    private void setupGlobalFlags(HashMap<String, Object> flags) {
        if (flags.containsKey("encoding"))
            GlobalFlags.encoding = flags.get("encoding").toString();
        if (flags.containsKey("ignoreDocs"))
            GlobalFlags.ignoreDocs = parseBoolFlag(flags.get("ignoreDocs").toString());
        if (flags.containsKey("displayReturnValue"))
            GlobalFlags.displayReturnValue = parseBoolFlag(flags.get("displayReturnValue").toString());
        if (flags.containsKey("debugPort"))
            GlobalFlags.debugPort = (short) flags.get("debugPort");
    }

    private void setupDebugger() throws IOException {
        DebugServer.initialize(GlobalFlags.debugPort);
        DebugServer.awaitConnection();
        DebugServer.acceptInitial();
        DebugServer.startServer();
    }

    public QObject launch(String[] args) throws IOException, PreprocessorException,
            LexerException, ParserException, LauncherException {
        LaunchCommandParser launchCommandParser = new LaunchCommandParser(args);
        launchCommandParser.parseReceivedArgs();
        localFlags = launchCommandParser.getUserFlags();
        setupGlobalFlags(launchCommandParser.getGlobalFlags());
        String mode = launchCommandParser.getSelectedRunStrategy();

        if (mode.equals("info")) {
            System.out.println("-=+* QuailJ by Tapeline *+=-");
            System.out.println("     Version: " +
                                QUAIL_MAJOR_VERSION + '.' +
                                QUAIL_MINOR_VERSION + '.' +
                                QUAIL_PATCH_VERSION + '-' +
                                QUAIL_VERSION_STATUS + '.' +
                                QUAIL_SUBVERSION);
            System.out.println("     " + (QUAIL_VERSION_SUFFIX == null? "Stable" : QUAIL_VERSION_SUFFIX));
            return null;
        }

        boolean doProfile = mode.equalsIgnoreCase("profile");
        boolean doDebug = mode.equalsIgnoreCase("debug");

        String code = FileUtils.readFileToString(new File(launchCommandParser.getTargetScript()),
                GlobalFlags.encoding);
        File scriptHome = new File(launchCommandParser.getTargetScript()).getAbsoluteFile().getParentFile();

        Preprocessor.resetDirectives();
        Preprocessor preprocessor = new Preprocessor(code, scriptHome);
        String preprocessedCode = preprocessor.preprocess();

        Lexer lexer = new Lexer(preprocessedCode);
        List<Token> tokens = lexer.scan();

        Parser parser = new Parser(preprocessedCode, tokens);
        BlockNode parsedCode = parser.parse();

        if (mode.equalsIgnoreCase("gendoc")) {
            if (launchCommandParser.getOutputFile() == null)
                throw new LauncherException("Output file for documentation is not specified");
            DocumentationGenerator generator = new DocumentationGenerator();
            String name = new File(launchCommandParser.getTargetScript()).getName();
            if (localFlags.containsKey("documentationHeadline"))
                name = localFlags.get("documentationHeadline").toString();
            String documentation = generator.generateDocumentationForFile(
                    name,
                    new File(launchCommandParser.getTargetScript()),
                    parsedCode
            );
            FileUtils.writeStringToFile(new File(launchCommandParser.getOutputFile()),
                    documentation, GlobalFlags.encoding);
            return null;
        } else if (mode.equalsIgnoreCase("run") || mode.equalsIgnoreCase("profile")
            || mode.equalsIgnoreCase("debug")) {
            if (doDebug) {
                try {
                    setupDebugger();
                } catch (IOException e) {
                    System.err.println("Tried to initialize debugger, but got IOException");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            Runtime runtime = new Runtime(parsedCode, preprocessedCode,
                    new File(launchCommandParser.getTargetScript()),
                    scriptHome, new DefaultIO(), doProfile, doDebug);
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
            if (doDebug) DebugServer.markProgramEnd();
            return returnValue;
        }

        throw new LauncherException("Invalid mode " + mode);
    }
    public QObject launchWithAnonymousCode(String code, File scriptHome, String[] args)
            throws LauncherException, PreprocessorException, LexerException, ParserException {
        return launchWithAnonymousCode(code, scriptHome, args, new DefaultIO());
    }

    public QObject launchWithAnonymousCode(String code, File scriptHome, String[] args, IO io)
            throws LauncherException, PreprocessorException, LexerException, ParserException {
        LaunchCommandParser launchCommandParser = new LaunchCommandParser(args);
        launchCommandParser.parseReceivedArgs();
        localFlags = launchCommandParser.getUserFlags();
        setupGlobalFlags(launchCommandParser.getGlobalFlags());
        String mode = launchCommandParser.getSelectedRunStrategy();

        if (!mode.equalsIgnoreCase("run") && !mode.equalsIgnoreCase("profile")
            && !mode.equalsIgnoreCase("debug"))
            throw new LauncherException("Invalid mode " + mode);
        boolean doProfile = mode.equalsIgnoreCase("profile");
        boolean doDebug = mode.equalsIgnoreCase("debug");

        Preprocessor preprocessor = new Preprocessor(code, scriptHome);
        String preprocessedCode = preprocessor.preprocess();

        Lexer lexer = new Lexer(preprocessedCode);
        List<Token> tokens = lexer.scan();

        Parser parser = new Parser(preprocessedCode, tokens);
        BlockNode parsedCode = parser.parse();

        Runtime runtime = new Runtime(parsedCode, preprocessedCode, new File(""),
                scriptHome, io, doProfile, doDebug);
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
