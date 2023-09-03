package me.tapeline.quailj.runtime.std.event.eventmanager;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class EventManager extends QObject {

    public static EventManager prototype = null;
    public static EventManager prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new EventManager(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new EventManagerConstructor(runtime)),
                            new Pair<>("addHandler", new EventManagerFuncAddHandler(runtime)),
                            new Pair<>("removeHandler", new EventManagerFuncRemoveHandler(runtime)),
                            new Pair<>("fireEvent", new EventManagerFuncFireEvent(runtime)),
                            new Pair<>("handleEvents", new EventManagerFuncHandleEvents(runtime))
                    )),
                    "EventManager",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Queue<Event> eventQueue;
    protected HashMap<String, List<QFunc>> eventHandlers;

    public EventManager(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new EventManager(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new EventManager(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new EventManager(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public Queue<Event> getEventQueue() {
        return eventQueue;
    }

    public HashMap<String, List<QFunc>> getEventHandlers() {
        return eventHandlers;
    }

    public void fireEvent(Event event) {
        eventQueue.add(event);
    }

}
