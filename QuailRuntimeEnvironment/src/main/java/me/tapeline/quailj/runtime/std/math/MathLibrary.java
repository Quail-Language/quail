package me.tapeline.quailj.runtime.std.math;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.storage.StorageLoadJson;
import me.tapeline.quailj.runtime.std.storage.StorageLoadYaml;
import me.tapeline.quailj.runtime.std.storage.StorageSaveJson;
import me.tapeline.quailj.runtime.std.storage.StorageSaveYaml;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class MathLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/math";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("product", new MathFuncProduct(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
