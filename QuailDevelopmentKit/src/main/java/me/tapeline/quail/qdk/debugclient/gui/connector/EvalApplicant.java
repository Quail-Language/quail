package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Base64;

public class EvalApplicant extends AbstractApplicant {

    private String code;
    private String result;

    public EvalApplicant(String code) {
        this.code = code;
    }

    @Override
    public boolean appliesToAnswer(String message) {
        return message.equalsIgnoreCase("evalresult");
    }

    @Override
    public void sendRequest(BufferedReader in, BufferedWriter out) throws IOException {
        out.write("eval\n");
        out.write(Base64.getEncoder().encodeToString(code.getBytes()) + "\n");
        out.flush();
    }

    @Override
    public void handleAnswer(BufferedReader in, BufferedWriter out) throws IOException {
        result = in.readLine();
    }

    public String getResult() {
        return result;
    }

}
