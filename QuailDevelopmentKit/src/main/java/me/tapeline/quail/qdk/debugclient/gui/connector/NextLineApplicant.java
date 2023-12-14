package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Base64;

public class NextLineApplicant extends AbstractApplicant implements Unanswerable {

    public NextLineApplicant() { }

    @Override
    public boolean appliesToAnswer(String message) {
        return false;
    }

    @Override
    public void sendRequest(BufferedReader in, BufferedWriter out) throws IOException {
        out.write("next\n");
        out.flush();
    }

    @Override
    public void handleAnswer(BufferedReader in, BufferedWriter out) throws IOException { }

}
