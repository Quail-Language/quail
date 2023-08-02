package me.tapeline.quailj.runtime.std.qml;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.qml.font.QMLFont;
import me.tapeline.quailj.runtime.std.qml.surface.QMLSurface;
import me.tapeline.quailj.runtime.std.qml.window.QMLWindow;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class QMLLibrary implements BuiltinLibrary {
    @Override
    public String id() {
        return "lang/qml";
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("Window", QMLWindow.prototype(runtime));
        contents.put("Surface", QMLSurface.prototype(runtime));
        contents.put("Font", QMLFont.prototype(runtime));
        return QObject.Val(contents);
    }
}
