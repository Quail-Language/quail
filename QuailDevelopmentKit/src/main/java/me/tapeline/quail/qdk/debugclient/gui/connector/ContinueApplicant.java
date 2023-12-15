package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ContinueApplicant extends AbstractApplicant implements Unanswerable {

    public ContinueApplicant() { }

    @Override
    public boolean appliesToAnswer(String message) {
        return false;
    }

    @Override
    public void sendRequest(BufferedReader in, BufferedWriter out) throws IOException {
        out.write("continue\n");
        out.flush();
    }

    @Override
    public void handleAnswer(BufferedReader in, BufferedWriter out) throws IOException { }

}
