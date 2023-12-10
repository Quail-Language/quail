package me.tapeline.quailj.parsing.annotation;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decoration {

    private Annotation annotation;
    private Node name;
    private final List<Node> args;
    private final Token token;

    public Decoration(Token token, Node name, List<Node> args) {
        this.token = token;
        this.name = name;
        this.args = args;
    }

    public Decoration(Token token, Annotation annotation, List<Node> args) {
        this.token = token;
        this.annotation = annotation;
        this.args = args;
    }

    public Node getName() {
        return name;
    }

    public List<Node> getArgs() {
        return args;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Token getToken() {
        return token;
    }

    public Object[] transformArgs() {
        Object[] args = new Object[this.args.size()];
        for (int i = 0; i < args.length; i++)
            args[i] = transform(this.args.get(i));
        return args;
    }

    private Object transform(Node node) {
        if (node instanceof LiteralNull)
            return null;
        if (node instanceof LiteralNum)
            return ((LiteralNum) node).value;
        if (node instanceof LiteralStr)
            return ((LiteralStr) node).value;
        if (node instanceof LiteralBool)
            return ((LiteralBool) node).value;
        if (node instanceof LiteralDict) {
            HashMap<String, Object> dict = new HashMap<>();
            for (int i = 0; i < ((LiteralDict) node).keys.size(); i++) {
                Object key = transform(((LiteralDict) node).keys.get(i));
                dict.put(key != null? key.toString() : "null",
                        transform(((LiteralDict) node).values.get(i)));
            }
            return dict;
        }
        if (node instanceof LiteralList) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < ((LiteralList) node).values.size(); i++)
                list.add(transform(((LiteralList) node).values.get(i)));
            return list;
        }
        return null;
    }

}
