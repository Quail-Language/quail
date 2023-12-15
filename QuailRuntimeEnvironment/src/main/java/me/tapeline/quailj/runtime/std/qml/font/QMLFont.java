package me.tapeline.quailj.runtime.std.qml.font;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.utils.Initializable;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QMLFont extends QObject implements Initializable {

    public static QMLFont prototype = null;
    public static QMLFont prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new QMLFont(
                    new Table(Dict.make(
                            new Pair<>("STYLE", Val(Dict.make(
                                    new Pair<>("PLAIN", Val(0)),
                                    new Pair<>("BOLD", Val(1)),
                                    new Pair<>("ITALIC", Val(2)),
                                    new Pair<>("BOLD_ITALIC", Val(3))
                            ))),
                            new Pair<>("_constructor", new FontConstructor(runtime)),
                            new Pair<>("getName", new FontFuncGetName(runtime)),
                            new Pair<>("getStyle", new FontFuncGetStyle(runtime)),
                            new Pair<>("getSize", new FontFuncGetSize(runtime)),
                            new Pair<>("getFontMetrics", new FontFuncGetFontMetrics(runtime))
                    )),
                    "Font",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Font font;

    public boolean isInitialized() {
        return font != null;
    }

    public QMLFont(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QMLFont(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QMLFont(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLFont(table, className, parent, isPrototype);
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
