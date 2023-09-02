package me.tapeline.quailj.runtime.std.qml.window;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManager;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.Initializable;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QMLWindow extends QObject implements Initializable {

    public static QMLWindow prototype = null;
    public static QMLWindow prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new QMLWindow(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new WindowConstructor(runtime)),
                            new Pair<>("centerOnScreen", new WindowFuncCenterOnScreen(runtime)),
                            new Pair<>("draw", new WindowFuncDraw(runtime)),
                            new Pair<>("getEventManager", new WindowFuncGetEventManager(runtime)),
                            new Pair<>("getHeight", new WindowFuncGetHeight(runtime)),
                            new Pair<>("getPositionX", new WindowFuncGetPositionX(runtime)),
                            new Pair<>("getPositionY", new WindowFuncGetPositionY(runtime)),
                            new Pair<>("getTitle", new WindowFuncGetTitle(runtime)),
                            new Pair<>("getWidth", new WindowFuncGetWidth(runtime)),
                            new Pair<>("isFocused", new WindowFuncIsFocused(runtime)),
                            new Pair<>("isFullscreen", new WindowFuncIsFullscreen(runtime)),
                            new Pair<>("isResizable", new WindowFuncIsResizable(runtime)),
                            new Pair<>("isVisible", new WindowFuncIsVisible(runtime)),
                            new Pair<>("requestFocus", new WindowFuncRequestFocus(runtime)),
                            new Pair<>("setFullscreen", new WindowFuncSetFullscreen(runtime)),
                            new Pair<>("setPosition", new WindowFuncSetPosition(runtime)),
                            new Pair<>("setResizable", new WindowFuncSetResizable(runtime)),
                            new Pair<>("setSize", new WindowFuncSetSize(runtime)),
                            new Pair<>("setTitle", new WindowFuncSetTitle(runtime)),
                            new Pair<>("setVisible", new WindowFuncSetVisible(runtime))
                    )),
                    "Window",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Frame frame;
    protected Canvas canvas;
    protected EventManager eventManager;
    protected MouseHandler mouseHandler;
    protected WindowHandler windowHandler;

    public boolean isInitialized() {
        return eventManager != null && frame != null && canvas != null;
    }

    public QMLWindow(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLWindow(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLWindow(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLWindow(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public static class MouseHandler implements MouseListener {

        public volatile boolean mouseDown = false;
        public volatile int mouseButton = -1;

        public QMLWindow window;
        public Runtime runtime;

        public MouseHandler(Runtime runtime, QMLWindow window) {
            this.window = window;
            this.runtime = runtime;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                QObject event = Event.prototype(runtime).newObject(runtime,
                        new ArrayList<>(Arrays.asList(Val("qml.mouseClick"))), new HashMap<>());
                event.set(runtime, "button", Val(e.getButton()));
                event.set(runtime, "clickCount", Val(e.getClickCount()));
                event.set(runtime, "x", Val(e.getX()));
                event.set(runtime, "y", Val(e.getY()));
                window.eventManager.fireEvent(((Event) event));
            } catch (RuntimeStriker ignored) {}
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseDown = true;
            mouseButton = e.getButton();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDown = false;
            mouseButton = -1;
        }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }

    public static class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            e.getWindow().dispose();
        }
    }

}
