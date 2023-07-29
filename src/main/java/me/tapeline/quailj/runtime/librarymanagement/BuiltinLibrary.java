package me.tapeline.quailj.runtime.librarymanagement;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;

public interface BuiltinLibrary {

    String id();
    QObject constructLibrary(Runtime runtime);

}
