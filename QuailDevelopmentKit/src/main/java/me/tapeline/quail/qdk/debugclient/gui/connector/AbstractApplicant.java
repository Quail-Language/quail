package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public abstract class AbstractApplicant {

    public abstract boolean appliesToAnswer(String message);
    public abstract void sendRequest(BufferedReader in, BufferedWriter out) throws IOException;
    public abstract void handleAnswer(BufferedReader in, BufferedWriter out) throws IOException;

}
