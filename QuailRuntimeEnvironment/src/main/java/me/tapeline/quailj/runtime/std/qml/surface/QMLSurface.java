package me.tapeline.quailj.runtime.std.qml.surface;

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
import java.awt.image.BufferedImage;

public class QMLSurface extends QObject implements Initializable {

    public static QMLSurface prototype = null;
    public static QMLSurface prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new QMLSurface(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new SurfaceConstructor(runtime)),
                            new Pair<>("clear", new SurfaceFuncClear(runtime)),
                            new Pair<>("drawLine", new SurfaceFuncDrawLine(runtime)),
                            new Pair<>("drawOval", new SurfaceFuncDrawOval(runtime)),
                            new Pair<>("drawPixel", new SurfaceFuncDrawPixel(runtime)),
                            new Pair<>("drawPoly", new SurfaceFuncDrawPoly(runtime)),
                            new Pair<>("drawRect", new SurfaceFuncDrawRect(runtime)),
                            new Pair<>("drawText", new SurfaceFuncDrawText(runtime)),
                            new Pair<>("fillOval", new SurfaceFuncFillOval(runtime)),
                            new Pair<>("fillPoly", new SurfaceFuncFillPoly(runtime)),
                            new Pair<>("fillRect", new SurfaceFuncFillRect(runtime)),
                            new Pair<>("getColor", new SurfaceFuncGetColor(runtime)),
                            new Pair<>("getColorHSB", new SurfaceFuncGetColorHSB(runtime)),
                            new Pair<>("getColorRGBA", new SurfaceFuncGetColorRGBA(runtime)),
                            new Pair<>("getFont", new SurfaceFuncGetFont(runtime)),
                            new Pair<>("getHeight", new SurfaceFuncGetHeight(runtime)),
                            new Pair<>("getWidth", new SurfaceFuncGetWidth(runtime)),
                            new Pair<>("setColor", new SurfaceFuncSetColor(runtime)),
                            new Pair<>("setColorHSB", new SurfaceFuncSetColorHSB(runtime)),
                            new Pair<>("setColorRGBA", new SurfaceFuncSetColorRGBA(runtime)),
                            new Pair<>("setFont", new SurfaceFuncSetFont(runtime)),
                            new Pair<>("stamp", new SurfaceFuncStamp(runtime))
                    )),
                    "Surface",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Graphics2D graphics;
    protected BufferedImage image;

    public boolean isInitialized() {
        return graphics != null && image != null;
    }

    public QMLSurface(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public void initForImage(BufferedImage image) {
        this.image = image;
        graphics = image.createGraphics();
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QMLSurface(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QMLSurface(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLSurface(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public BufferedImage getImage() {
        return image;
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
