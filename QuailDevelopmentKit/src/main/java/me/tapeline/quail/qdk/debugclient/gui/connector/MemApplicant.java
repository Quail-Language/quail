package me.tapeline.quail.qdk.debugclient.gui.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class MemApplicant extends AbstractApplicant {

    private HashMap<String, String> result;

    public MemApplicant() { }

    @Override
    public boolean appliesToAnswer(String message) {
        return message.equalsIgnoreCase("memresult");
    }

    @Override
    public void sendRequest(BufferedReader in, BufferedWriter out) throws IOException {
        out.write("mem\n");
        out.flush();
    }

    @Override
    public void handleAnswer(BufferedReader in, BufferedWriter out) throws IOException {
        String result = in.readLine();
        if (result.equalsIgnoreCase("mem not present")) {
            result = null;
            return;
        }
        int iCount = Integer.parseInt(result);
        this.result = new HashMap<>();
        for (int i = 0; i < iCount; i++) {
            try {
                String entry = in.readLine();
                if (entry.equalsIgnoreCase("endmem"))
                    break;
                String key = new String(Base64.getDecoder().decode(
                        entry.substring(0, entry.indexOf(';'))));
                String value = new String(Base64.getDecoder().decode(
                        entry.substring(entry.indexOf(';') + 1)));
                this.result.put(key, value);
            } catch (Exception ignored) {}
        }
    }

    public HashMap<String, String> getResult() {
        return result;
    }

}
