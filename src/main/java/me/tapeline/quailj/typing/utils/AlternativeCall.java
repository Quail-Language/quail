package me.tapeline.quailj.typing.utils;

import me.tapeline.quailj.parsing.nodes.Node;

import java.util.List;

public class AlternativeCall {

    private List<FuncArgument> arguments;
    private Node code;

    public AlternativeCall(List<FuncArgument> arguments, Node code) {
        this.arguments = arguments;
        this.code = code;
    }

    public List<FuncArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<FuncArgument> arguments) {
        this.arguments = arguments;
    }

    public Node getCode() {
        return code;
    }

    public void setCode(Node code) {
        this.code = code;
    }

}
