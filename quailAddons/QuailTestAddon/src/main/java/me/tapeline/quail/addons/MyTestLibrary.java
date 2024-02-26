package me.tapeline.quail.addons;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class MyTestLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "qta/mytestlib";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("test", new MyTestFuncTest(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
