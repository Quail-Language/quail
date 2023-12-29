package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.fs.*;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class StorageLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/storage";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("loadJson", new StorageLoadJson(runtime));
        contents.put("loadYaml", new StorageLoadYaml(runtime));
        contents.put("saveJson", new StorageSaveJson(runtime));
        contents.put("saveYaml", new StorageSaveYaml(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
