package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.runtime.std.ji.javamethod.JavaMethod;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class JILibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/ji";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) throws RuntimeStriker {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("JavaClass", JavaClass.prototype(runtime));
        contents.put("JavaMethod", JavaMethod.prototype(runtime));
        contents.put("JavaObject", JavaObject.prototype(runtime));
        contents.put("getClass", new JIFuncGetClass(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
