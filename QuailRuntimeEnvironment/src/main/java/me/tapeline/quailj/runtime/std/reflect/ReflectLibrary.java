package me.tapeline.quailj.runtime.std.reflect;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.storage.StorageLoadJson;
import me.tapeline.quailj.runtime.std.storage.StorageLoadYaml;
import me.tapeline.quailj.runtime.std.storage.StorageSaveJson;
import me.tapeline.quailj.runtime.std.storage.StorageSaveYaml;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class ReflectLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/reflect";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("setBoolValue", new ReflectSetBoolValue(runtime));
        contents.put("setFuncValue", new ReflectSetFuncValue(runtime));
        contents.put("setNumberValue", new ReflectSetNumberValue(runtime));
        contents.put("setStringValue", new ReflectSetStringValue(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
