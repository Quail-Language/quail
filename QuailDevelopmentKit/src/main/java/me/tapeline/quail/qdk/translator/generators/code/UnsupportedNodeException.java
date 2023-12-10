package me.tapeline.quail.qdk.translator.generators.code;

public class UnsupportedNodeException extends Exception {

    private final Class<?> nodeClass;

    public UnsupportedNodeException(Class<?> nodeClass) {
        super("Nodes of class " + nodeClass.getSimpleName() + " are not supported");
        this.nodeClass = nodeClass;
    }

}
