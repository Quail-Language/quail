package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.runtime.std.ji.javamethod.JavaMethod;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.runtime.std.ji.sketchedjavaclass.SketchedJavaClass;
import me.tapeline.quailj.runtime.std.ji.sketchedjavaconstructor.SketchedJavaConstructor;
import me.tapeline.quailj.runtime.std.ji.sketchedjavafield.SketchedJavaField;
import me.tapeline.quailj.runtime.std.ji.sketchedjavainheritance.SketchedJavaInheritance;
import me.tapeline.quailj.runtime.std.ji.sketchedjavamethod.SketchedJavaMethod;
import me.tapeline.quailj.runtime.std.ji.sketchedjavapackage.SketchedJavaPackage;
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
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("JavaClass", JavaClass.prototype(runtime));
        contents.put("JavaMethod", JavaMethod.prototype(runtime));
        contents.put("JavaObject", JavaObject.prototype(runtime));

        contents.put("SketchedJavaClass", SketchedJavaClass.prototype(runtime));
        contents.put("SketchedJavaField", SketchedJavaField.prototype(runtime));
        contents.put("SketchedJavaMethod", SketchedJavaMethod.prototype(runtime));
        contents.put("SketchedJavaPackage", SketchedJavaPackage.prototype(runtime));
        contents.put("SketchedJavaConstructor", SketchedJavaConstructor.prototype(runtime));
        contents.put("SketchedJavaInheritance", SketchedJavaInheritance.prototype(runtime));

        contents.put("JavaException", JIJavaException.prototype);

        contents.put("getClass", new JIFuncGetClass(runtime));
        contents.put("deployPackage", new JIFuncDeployPackage(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
