package me.tapeline.quailj.runtime.std.basic.threading;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;
import java.util.List;

public class QuailThread extends Thread {

    protected QFunc runnable;
    protected Runtime runtime;
    protected List<QObject> args;
    protected QObject returnValue;

    public QuailThread(Runtime runtime, QFunc function, List<QObject> args) throws RuntimeStriker {
        this.runtime = runtime;
        runnable = function;
        this.args = args;
    }

    public void forceSleep(long t) {
        try {
            sleep(t);
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void run() {
        try {
            returnValue = runnable.call(runtime, args, new HashMap<>());
        } catch (RuntimeStriker e) {
            if (e.getType().equals(RuntimeStriker.Type.EXCEPTION)) {
                HashMap<String, QObject> data = new HashMap<>();
                data.put("error", QObject.Val(true));
                data.put("message", e.getCarryingError());
                returnValue = QObject.Val(data);
            }
        }
    }

    public QFunc getRunnable() {
        return runnable;
    }

    public Runtime getRuntime() {
        return runtime;
    }

    public List<QObject> getArgs() {
        return args;
    }

    public QObject getReturnValue() {
        return returnValue;
    }

}
