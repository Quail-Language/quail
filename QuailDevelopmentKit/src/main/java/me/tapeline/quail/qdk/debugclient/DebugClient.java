package me.tapeline.quail.qdk.debugclient;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class DebugClient {

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader socketIn;
    private BufferedWriter socketOut;

    private boolean isRunning = true;

    public DebugClient(String host, short port) throws IOException {
        clientSocket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(System.in));
        socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        socketOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        System.out.println("Connected to remote debug session on " + host + ":" + port);
    }

    public void run() {
        try {
            try {
                System.out.println("Specify breakpoints in format filename.ext;a;b;c where a,b,c - line numbers.");
                System.out.println("When you're done, type end");
                Scanner scanner = new Scanner(System.in);
                String bp;
                List<String> breakpoints = new ArrayList<>();
                while (true) {
                    bp = scanner.nextLine();
                    if (bp.equalsIgnoreCase("end")) break;
                    breakpoints.add(bp);
                }
                for (String breakpoint : breakpoints)
                    socketOut.write(breakpoint + "\n");
                socketOut.write("endheader\n");
                socketOut.flush();
                while (isRunning) {
                    String messageType = socketIn.readLine();
                    if (messageType.equalsIgnoreCase("bp")) {
                        handleBpInstruction();
                    } else if (messageType.equalsIgnoreCase("end")) {
                        System.out.println("Program ended");
                        isRunning = false;
                    }
                }
            } finally {
                System.out.println("Client closed");
                clientSocket.close();
                socketIn.close();
                socketOut.close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private void handleBpInstruction() throws IOException {
        String message = socketIn.readLine();
        String file = message.substring(0, message.indexOf(';'));
        int line = Integer.parseInt(message.substring(message.indexOf(';') + 1));
        System.out.println("Breakpoint reached at " + file + " line " + line);
        while (true) {
            System.out.print("qsdb> ");
            BufferedReader inputScanner = new BufferedReader(new InputStreamReader(System.in));
            while (!inputScanner.ready()) {
                if (socketIn.ready()) {
                    String msg = socketIn.readLine();
                    if (msg.equalsIgnoreCase("end")) {
                        System.out.println("Program ended");
                        isRunning = false;
                        return;
                    } else if (msg.equalsIgnoreCase("bpe")) {
                        return;
                    }
                }
            }
            String command = inputScanner.readLine();
            if (command.equalsIgnoreCase("mem")) {
                socketOut.write("mem\n");
                socketOut.flush();
                String count = socketIn.readLine();
                if (count.equalsIgnoreCase("mem not present")) {
                    System.out.println("Memory is not available at the moment");
                    continue;
                }
                int iCount = Integer.parseInt(count);
                for (int i = 0; i < iCount; i++) {
                    try {
                        String entry = socketIn.readLine();
                        if (entry.equalsIgnoreCase("endmem"))
                            break;
                        String key = new String(Base64.getDecoder().decode(
                                entry.substring(0, entry.indexOf(';'))));
                        String value = new String(Base64.getDecoder().decode(
                                entry.substring(entry.indexOf(';') + 1)));
                        System.out.println(key + " -> " + value);
                    } catch (Exception ignored) {}
                }
            } else if (command.equalsIgnoreCase("c")) {
                socketOut.write("continue\n");
                socketOut.flush();
            } else if (command.equalsIgnoreCase("nl")) {
                socketOut.write("next\n");
                socketOut.flush();
            } else if (command.equalsIgnoreCase("stop")) {
                socketOut.write("stop\n");
                socketOut.flush();
                isRunning = false;
                return;
            } else if (command.equalsIgnoreCase("eval")) {
                System.out.print("eval> ");
                String code = inputScanner.readLine();
                socketOut.write("eval\n");
                socketOut.write(Base64.getEncoder().encodeToString(code.getBytes()) + "\n");
                socketOut.flush();
                socketIn.readLine();
                String result = socketIn.readLine();
                System.out.println(" ?= " + result);
            }
        }
    }

}
