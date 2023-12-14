package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class StopApplicant extends AbstractApplicant implements Unanswerable {

    public StopApplicant() { }

    @Override
    public boolean appliesToAnswer(String message) {
        return false;
    }

    @Override
    public void sendRequest(BufferedReader in, BufferedWriter out) throws IOException {
        out.write("stop\n");
        out.flush();
    }

    @Override
    public void handleAnswer(BufferedReader in, BufferedWriter out) throws IOException { }

}
