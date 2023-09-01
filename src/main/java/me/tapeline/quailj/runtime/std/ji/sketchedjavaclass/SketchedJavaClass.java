package me.tapeline.quailj.runtime.std.ji.sketchedjavaclass;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;


public class SketchedJavaClass extends QObject {

    public static SketchedJavaClass prototype = null;
    public static SketchedJavaClass prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new SketchedJavaClass(
                    new Table(Dict.make(
                            new Pair<>("_constructor",
                                    new SketchedJavaClassConstructor(runtime))
                    )),
                    "SketchedJavaClass",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private ClassSourceGenerator clazz;

    public ClassSourceGenerator getClazz() {
        return clazz;
    }

    public void setClass(ClassSourceGenerator clazz) {
        this.clazz = clazz;
    }

    public SketchedJavaClass(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public SketchedJavaClass(Table table, String className, QObject parent, boolean isPrototype,
                             ClassSourceGenerator clazz) {
        super(table, className, parent, isPrototype);
        this.clazz = clazz;
    }

    public SketchedJavaClass(ClassSourceGenerator clazz) {
        this(new Table(), prototype(null).className, prototype, false, clazz);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new SketchedJavaClass(new Table(), className, this, false,
                clazz);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new SketchedJavaClass(new Table(), className, this, true,
                clazz);
    }

    @Override
    public QObject copy() {
        QObject copy = new SketchedJavaClass(table, className, parent, isPrototype,
                clazz);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
