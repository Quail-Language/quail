package me.tapeline.quail.qdk.debugclient.gui;

import me.tapeline.quail.qdk.debugclient.gui.connector.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.function.BiConsumer;

public class GUIDebugClient {

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader socketIn;
    private BufferedWriter socketOut;
    private volatile boolean isRunning = true;
    public volatile List<BiConsumer<String, Integer>> breakpointReachListeners = new ArrayList<>();
    private ConnectorServer connector;

    public GUIDebugClient(String host, short port) throws IOException {
        clientSocket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(System.in));
        socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        socketOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        connector = new ConnectorServer(socketIn, socketOut);
        connector.addMessageReceivedHandler((message) -> {
            if (message.equalsIgnoreCase("bp")) {
                String fileAndLine = null;
                try {
                    fileAndLine = socketIn.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String file = fileAndLine.substring(0, fileAndLine.indexOf(';'));
                int line = Integer.parseInt(fileAndLine.substring(
                        fileAndLine.indexOf(';') + 1));
                for (BiConsumer<String, Integer> listener : breakpointReachListeners)
                    listener.accept(file, line);
            }
        });
    }

    public String evalExpression(String expr) throws IOException {
        connector.addApplicant(new EvalApplicant(expr));
        while (true) {
            for (AbstractApplicant applicant : connector.getFulfilledQueue()) {
                if (applicant instanceof EvalApplicant) {
                    connector.getFulfilledQueue().remove(applicant);
                    return ((EvalApplicant) applicant).getResult();
                }
            }
        }
    }

    public void nextLine() throws IOException {
        connector.addApplicant(new NextLineApplicant());
    }

    public void resumeRunning() throws IOException {
        connector.addApplicant(new ContinueApplicant());
    }

    public void stopDebugger() throws IOException {
        connector.addApplicant(new StopApplicant());
    }

    public void sendInitialBreakpoints(HashMap<String, Set<Integer>> breakpoints) throws IOException {
        for (Map.Entry<String, Set<Integer>> entry : breakpoints.entrySet()) {
            socketOut.write(entry.getKey());
            for (Integer line : entry.getValue()) socketOut.write(";" + line);
            socketOut.write("\n");
        }
        socketOut.write("endheader\n");
        socketOut.flush();
    }

    public HashMap<String, String> getScopeContents() throws IOException {
        connector.addApplicant(new MemApplicant());
        while (true) {
            for (AbstractApplicant applicant : connector.getFulfilledQueue()) {
                if (applicant instanceof MemApplicant) {
                    connector.getFulfilledQueue().remove(applicant);
                    return ((MemApplicant) applicant).getResult();
                }
            }
        }
    }

}
