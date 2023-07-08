package me.tapeline.quailj.runtime;


import me.tapeline.quailj.parsing.nodes.Node;

public class AsyncRuntimeWorker extends Thread {

    public Node node;
    public Runtime runtime;
    public Memory scope;

    public AsyncRuntimeWorker(Node node, Runtime runtime, Memory scope) {
        this.node = node;
        this.runtime = runtime;
        this.scope = scope;
    }

    @Override
    public void run() {
        /*try {
            runtime.run(node, scope);
        } catch (RuntimeStriker striker) {
            if (striker.type == RuntimeStriker.Type.EXCEPTION ||
                    striker.type == RuntimeStriker.Type.EXIT) {
                System.err.println(striker.error.toString());
            }
        }*/
    }

}