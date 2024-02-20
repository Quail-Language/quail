package me.tapeline.quailj.runtime.std.cli.argparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.tapeline.quailj.runtime.std.cli.argparser.ArgTokenType.*;

public class ArgParser {

    private List<ArgToken> tokens;
    private int pos = 0;
    private HashMap<String, Object> kwargs = new HashMap<>();
    private HashMap<String, Boolean> boolFlags = new HashMap<>();
    private List<Object> arguments = new ArrayList<>();

    public ArgParser(List<ArgToken> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (pos < tokens.size()) {
            ArgToken token = tokens.get(pos++);
            if (token.getType().equals(BOOL_FLAG)) {
                boolean value = token.getLexeme().charAt(0) == '+';
                for (int i = 1; i < token.getLexeme().length(); i++)
                    boolFlags.put(String.valueOf(token.getLexeme().charAt(i)), value);
            } else if (token.getType().equals(KEYWORD_FLAG)) {
                String name = token.getLexeme().substring(2);
                if (pos < tokens.size() && tokens.get(pos).getType() == EQUALS) {
                    pos++;
                    if (pos >= tokens.size()) return;
                    ArgToken value = tokens.get(pos++);
                    if (value.getType() == NUMBER)
                        kwargs.put(name, Double.valueOf(value.getLexeme()));
                    else if (value.getType() == BOOL)
                        kwargs.put(name, value.getLexeme().equalsIgnoreCase("true"));
                    else if (value.getType() == STRING)
                        kwargs.put(name, value.getLexeme().substring(1, value.getLexeme().length() - 1));
                    else if (value.getType() == ARGUMENT) {
                        kwargs.put(name, value.getLexeme());
                    }
                } else return;
            } else {
                if (token.getType() == NUMBER)
                    arguments.add(Double.valueOf(token.getLexeme()));
                else if (token.getType() == BOOL)
                    arguments.add(token.getLexeme().equalsIgnoreCase("true"));
                else if (token.getType() == STRING)
                    arguments.add(token.getLexeme().substring(1, token.getLexeme().length() - 1));
                else if (token.getType() == ARGUMENT) {
                    arguments.add(token.getLexeme());
                }
            }
        }
    }

    public HashMap<String, Object> getKwargs() {
        return kwargs;
    }

    public HashMap<String, Boolean> getBoolFlags() {
        return boolFlags;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
