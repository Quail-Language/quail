package me.tapeline.quailj.runtime;


import me.tapeline.quailj.parsing.nodes.Node;

public class AsyncRuntimeWorker extends Thread {

    private final Node node;
    private final Runtime runtime;
    private final Memory scope;

    public AsyncRuntimeWorker(Node node, Runtime runtime, Memory scope) {
        this.node = node;
        this.runtime = runtime;
        this.scope = scope;
    }

    @Override
    public void run() {
        try {
            runtime.run(node, scope);
        } catch (RuntimeStriker striker) {
            if (striker.getType() == RuntimeStriker.Type.EXCEPTION)
                System.err.println(striker.formatError(runtime.getCode()));
        }
    }

}