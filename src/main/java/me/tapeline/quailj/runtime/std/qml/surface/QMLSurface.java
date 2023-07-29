package me.tapeline.quailj.runtime.std.qml.surface;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.qml.window.*;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class QMLSurface extends QObject {

    public static QMLSurface prototype = null;
    public static QMLSurface prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new QMLSurface(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new WindowConstructor(runtime)),
                            new Pair<>("centerOnScreen", new WindowFuncCenterOnScreen(runtime)),
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
                    "Surface",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Graphics2D graphics;
    protected BufferedImage image;

    public QMLSurface(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLSurface(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLSurface(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLSurface(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public static class MouseHandler implements MouseListener {

        public volatile boolean mouseDown = false;
        public volatile int mouseButton = -1;

        public MouseHandler() { }

        @Override
        public void mouseClicked(MouseEvent e) { }

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
