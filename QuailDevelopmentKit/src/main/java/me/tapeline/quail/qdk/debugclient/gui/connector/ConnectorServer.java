package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class ConnectorServer {

    private Queue<AbstractApplicant> outcomingQueue = new ArrayDeque<>();
    private Queue<AbstractApplicant> incomingQueue = new ArrayDeque<>();
    private Queue<AbstractApplicant> fulfilledQueue = new ArrayDeque<>();
    private List<Consumer<AbstractApplicant>> applicantFulfilledListeners = new ArrayList<>();
    private List<Consumer<String>> messageReceivedHandlers = new ArrayList<>();

    private BufferedReader in;
    private BufferedWriter out;
    private Thread readerThread;

    public ConnectorServer(BufferedReader in, BufferedWriter out) {
        this.in = in;
        this.out = out;
        readerThread = new Thread(() -> {
            while (true)
                serverLoop();
        });
        readerThread.start();
    }

    public void shutdownServer() {
        readerThread.interrupt();
    }

    private void serverLoop() {
        try {
            String message = in.readLine();
            for (Consumer<String> listener : messageReceivedHandlers)
                listener.accept(message);
            handleIncomingMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleIncomingMessage(String message) {
        AbstractApplicant handler = null;
        for (AbstractApplicant applicant : incomingQueue) {
            if (applicant.appliesToAnswer(message)) {
                try {
                    applicant.handleAnswer(in, out);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                handler = applicant;
                break;
            }
        }
        if (handler == null) return;
        incomingQueue.remove(handler);
        fulfilledQueue.add(handler);
        AbstractApplicant finalHandler = handler;
        applicantFulfilledListeners.forEach(applicantConsumer -> applicantConsumer.accept(finalHandler));
    }

    private void handleOutcomingApplicant(AbstractApplicant applicant) {
        if (!(applicant instanceof Unanswerable))
            incomingQueue.add(applicant);
        try {
            applicant.sendRequest(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        outcomingQueue.remove(applicant);
    }

    public void addApplicant(AbstractApplicant applicant) {
        outcomingQueue.add(applicant);
        handleOutcomingApplicant(applicant);
    }

    public List<Consumer<AbstractApplicant>> getApplicantFulfilledListeners() {
        return applicantFulfilledListeners;
    }

    public void addApplicantFulfilledListener(Consumer<AbstractApplicant> listener) {
        applicantFulfilledListeners.add(listener);
    }

    public List<Consumer<String>> getMessageReceivedHandlers() {
        return messageReceivedHandlers;
    }

    public void addMessageReceivedHandler(Consumer<String> listener) {
        messageReceivedHandlers.add(listener);
    }

    public Queue<AbstractApplicant> getOutcomingQueue() {
        return outcomingQueue;
    }

    public Queue<AbstractApplicant> getIncomingQueue() {
        return incomingQueue;
    }

    public Queue<AbstractApplicant> getFulfilledQueue() {
        return fulfilledQueue;
    }

}
