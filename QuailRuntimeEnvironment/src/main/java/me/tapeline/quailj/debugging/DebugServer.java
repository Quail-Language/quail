package me.tapeline.quailj.debugging;

import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class DebugServer {

    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static volatile boolean isRunning = true;
    private static volatile boolean continueFlag = false;
    private static volatile boolean nextStepFlag = false;
    private static Thread serverThread;
    private static volatile Runtime currentRuntime;
    private static volatile Memory currentScope;

    /** Whether there was a socket exception and whether debugger can continue its work */
    public static boolean malfunction = false;

    /** Queue of code send via debugger for evaluation */
    private static volatile Queue<String> codeToBeExecuted = new ArrayDeque<>();

    public static HashMap<File, Set<Integer>> breakpoints = new HashMap<>();

    public static void initialize(short port) throws IOException {
        server = new ServerSocket(port);
    }

    public static void awaitConnection() throws IOException {
        System.out.println("Waiting for debug client to connect on port " + server.getLocalPort());
        clientSocket = server.accept();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()), 1000000);
    }

    /**
     * Accepts initial messages from debug client as described in documentation.
     * These include, but may not be limited to breakpoint information
     */
    public static void acceptInitial() throws IOException {
        String message = "";
        do {
            message = in.readLine();
            File file = new File(message.substring(0, message.indexOf(';')));
            message = message.substring(message.indexOf(';') + 1);
            Set<Integer> breakpoints = new HashSet<>();
            for (String s : message.split(";"))
                breakpoints.add(Integer.parseInt(s));
            DebugServer.breakpoints.put(file, breakpoints);
        } while (message.equalsIgnoreCase("endheader"));
    }

    public static void startServer() {
        isRunning = true;
        serverThread = new Thread(DebugServer::run);
        serverThread.start();
    }

    /**
     * States that program execution ended.
     * Writes corresponding message to client
     */
    public static void markProgramEnd() {
        if (malfunction) return;
        try {
            out.write("end\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        isRunning = false;
        serverThread.interrupt();
    }

    private static void run() {
        try {
            try {
                try {
                    while (isRunning)
                        acceptMessage();
                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                server.close();
            }
        } catch (SocketException e) {
            malfunction = true;
            System.err.println(e);
            System.err.println("Debug client disconnected. Execution continued as normal");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static Pair<File, Set<Integer>> acceptBreakpoints() throws IOException {
        String message = in.readLine();
        File file = new File(message.substring(0, message.indexOf(';')));
        message = message.substring(message.indexOf(';'));
        Set<Integer> breakpoints = new HashSet<>();
        for (String s : message.split(";"))
            breakpoints.add(Integer.parseInt(s));
        return new Pair<>(file, breakpoints);
    }

    private static void acceptMessage() throws IOException {
        String messageType = in.readLine();
        if (messageType.equalsIgnoreCase("ab")) {
            Pair<File, Set<Integer>> breakpoints = acceptBreakpoints();
            if (DebugServer.breakpoints.containsKey(breakpoints.key))
                DebugServer.breakpoints.get(breakpoints.key).addAll(breakpoints.value);
            else
                DebugServer.breakpoints.put(breakpoints.key, breakpoints.value);
        } else if (messageType.equalsIgnoreCase("rb")) {
            Pair<File, Set<Integer>> breakpoints = acceptBreakpoints();
            if (DebugServer.breakpoints.containsKey(breakpoints.key)) {
                DebugServer.breakpoints.get(breakpoints.key).removeAll(breakpoints.value);
                if (DebugServer.breakpoints.get(breakpoints.key).isEmpty())
                    DebugServer.breakpoints.remove(breakpoints.key);
            }
        } else if (messageType.equalsIgnoreCase("stop")) {
            isRunning = false;
        } else if (messageType.equalsIgnoreCase("continue")) {
            continueFlag = true;
        } else if (messageType.equalsIgnoreCase("next")) {
            nextStepFlag = true;
            continueFlag = true;
        } else if (messageType.equalsIgnoreCase("mem")) {
            sendMemContents();
        } else if (messageType.equalsIgnoreCase("eval")) {
            String message = in.readLine();
            String code = new String(Base64.getDecoder().decode(message));
            codeToBeExecuted.add(code);
        }
    }

    private static void sendMemContents() throws IOException {
        if (currentScope == null) {
            out.write("memresult\n");
            out.write("mem not present\n");
            out.flush();
            return;
        }
        out.write("memresult\n");
        Set<Map.Entry<String, QObject>> entries = currentScope.getAllEntries();
        out.write((entries.size() + 1) + "\n");
        for (Map.Entry<String, QObject> entry : entries) {
            try {
                String key = Base64.getEncoder().encodeToString(entry.getKey().getBytes());
                String value = Base64.getEncoder().encodeToString(entry.getValue().toString().getBytes());
                out.write(key + ";" + value + "\n");
            } catch (Exception ignored) {}
        }
        out.write("endmem\n");
        out.flush();
    }

    /**
     * Called when program steps onto a breakpoint
     */
    public static void enterBreakpoint(Runtime runtime, Memory scope, int line) throws IOException {
        out.write("bp\n");
        out.write(runtime.getScriptFile() + ";" + line +"\n");
        out.flush();
        currentRuntime = runtime;
        currentScope = scope;
        while (!continueFlag) {
            if (malfunction) return;
            if (!codeToBeExecuted.isEmpty()) {
                String code = codeToBeExecuted.poll();
                try {
                    QObject result = runtime.evalExpression(code, scope);
                    out.write("evalresult\n");
                    out.write(result.toString() + "\n");
                    out.flush();
                } catch (PreprocessorException | ParserException | LexerException e) {
                    out.write("evalresult\n");
                    out.write(Base64.getEncoder().encodeToString(e.toString().getBytes()) + "\n");
                    out.flush();
                }
            }
        }
        continueFlag = false;
        if (nextStepFlag)
            runtime.nextLineToStop = line + 1;
        currentRuntime = null;
        currentScope = null;
        out.write("bpe\n");
        out.flush();
    }

}
