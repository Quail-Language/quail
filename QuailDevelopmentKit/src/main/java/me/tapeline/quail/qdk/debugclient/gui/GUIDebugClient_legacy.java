package me.tapeline.quail.qdk.debugclient.gui;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.function.BiConsumer;

public class GUIDebugClient_legacy {

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader socketIn;
    private BufferedWriter socketOut;
    private Thread sessionThread;
    private volatile boolean isRunning = true;
    private volatile boolean canConsumeInput = true;
    public volatile List<BiConsumer<String, Integer>> breakpointReachListeners = new ArrayList<>();

    public GUIDebugClient_legacy(String host, short port) throws IOException {
        clientSocket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(System.in));
        socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        socketOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        sessionThread = new Thread(() -> {
            while (true) {
                try {
                    String data = Utils.getDataFromReader(socketIn);
                    if (canConsumeInput && data.startsWith("end\n") || data.startsWith("bp\n")) {
                        String message = socketIn.readLine();
                        if (message.equalsIgnoreCase("end")) {
                            isRunning = false;
                            return;
                        } else if (message.equalsIgnoreCase("bp")) {
                            String fileAndLine = socketIn.readLine();
                            String file = fileAndLine.substring(0, fileAndLine.indexOf(';'));
                            int line = Integer.parseInt(fileAndLine.substring(
                                    fileAndLine.indexOf(';') + 1));
                            for (BiConsumer<String, Integer> listener : breakpointReachListeners)
                                listener.accept(file, line);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        sessionThread.start();
    }

    private void pauseSessionThread() {
        canConsumeInput = false;
//        try {
//            sessionThread.wait();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void resumeSessionThread() {
        canConsumeInput = true;
//        sessionThread.notify();
    }

    public String evalExpression(String expr) throws IOException {
        pauseSessionThread();
        socketOut.write("eval\n");
        socketOut.write(Base64.getEncoder().encodeToString(expr.getBytes()) + "\n");
        socketOut.flush();
        String result = socketIn.readLine();
        resumeSessionThread();
        return result;
    }

    public void nextLine() throws IOException {
        socketOut.write("next\n");
        socketOut.flush();
    }

    public void resumeRunning() throws IOException {
        socketOut.write("continue\n");
        socketOut.flush();
    }

    public void stopDebugger() throws IOException {
        socketOut.write("stop\n");
        socketOut.flush();
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
        pauseSessionThread();
        socketOut.write("mem\n");
        socketOut.flush();
        String count = socketIn.readLine();
        if (count.equalsIgnoreCase("mem not present")) {
            resumeSessionThread();
            return null;
        }
        int iCount = Integer.parseInt(count);
        HashMap<String, String> mem = new HashMap<>();
        for (int i = 0; i < iCount; i++) {
            try {
                String entry = socketIn.readLine();
                if (entry.equalsIgnoreCase("endmem"))
                    break;
                String key = new String(Base64.getDecoder().decode(
                        entry.substring(0, entry.indexOf(';'))));
                String value = new String(Base64.getDecoder().decode(
                        entry.substring(entry.indexOf(';') + 1)));
                mem.put(key, value);
            } catch (Exception ignored) {}
        }
        resumeSessionThread();
        return mem;
    }

}
