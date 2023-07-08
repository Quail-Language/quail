package me.tapeline.quailj.runtime;

import me.tapeline.quailj.io.IO;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.effects.AsyncNode;
import me.tapeline.quailj.typing.classes.QObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Runtime {

    public final File scriptHome;
    private final Node root;
    private final boolean doProfile;
    public IO io;
    public Memory memory;
//    public EmbedIntegrator embedIntegrator;
//    public static LibraryRegistry libraryRegistry = new LibraryRegistry();
//    public List<AsyncRuntimeWorker> asyncRuntimeWorkers = new ArrayList<>();
//    public HashMap<String, List<Pair<QFunc, Boolean>>> eventsHandlerMap = new HashMap<>();
    public String[] consoleArgs;
    public List<QObject> consoleArgsQObjects = new ArrayList<>();
//
//    public static QObject superObject = QObject.constructSuperObject();
//    public static QObject numberPrototype = new QObject("Number", null, new HashMap<>());
//    public static QObject nullPrototype = new QObject("Null", null, new HashMap<>());
//    public static QObject stringPrototype = new QObject("String", null, new HashMap<>());
//    public static QObject listPrototype = new QObject("List", null, new HashMap<>());
//    public static QObject boolPrototype = new QObject("Bool", null, new HashMap<>());
//    public static QObject funcPrototype = new QObject("Func", null, new HashMap<>());

    public Runtime(File file, Node root, IO io, String[] consoleArgs, boolean doProfile) {
        this.scriptHome = file;
        this.root = root;
        this.doProfile = doProfile;
        this.consoleArgs = consoleArgs;
        for (String arg : consoleArgs)
            consoleArgsQObjects.add(QObject.Val(arg));
        this.io = io;
//        this.memory = new Memory();
//        registerDefaults();
//        libraryRegistry.libraryRoots.add("/home/tapeline/JavaProjects/Files/Quailv2/qlibs/?");
//        libraryRegistry.libraryRoots.add("$cwd$/?");
//        libraryRegistry.libraryRoots.add("$script$/?");
    }

    public static void error(String message) throws RuntimeStriker {
        throw new RuntimeStriker(
                /*new ErrorMessage(
                        Error.ERROR,
                        message
                )*/
                message
        );
    }

    private void begin(Node node) {
        node.executionStart = System.currentTimeMillis();
    }

    private void end(Node node) {
        node.executionTime = System.currentTimeMillis() - node.executionStart;
    }

    public QObject run(Node node) throws RuntimeStriker {
        return run(node, memory);
    }

    public QObject run(Node node, Memory memory) throws RuntimeStriker {
        return null;
    }


}
