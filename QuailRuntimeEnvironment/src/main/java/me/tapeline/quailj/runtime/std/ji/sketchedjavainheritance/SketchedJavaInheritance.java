package me.tapeline.quailj.runtime.std.ji.sketchedjavainheritance;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.burningwave.core.classes.VariableSourceGenerator;


public class SketchedJavaInheritance extends QObject {

    public static SketchedJavaInheritance prototype = null;
    public static SketchedJavaInheritance prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new SketchedJavaInheritance(
                    new Table(Dict.make(
                            new Pair<>("_constructor",
                                    new SketchedJavaInheritanceConstructor(runtime))
                    )),
                    "SketchedJavaInheritance",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private Class<?> extending;

    public Class<?> getExtending() {
        return extending;
    }

    public void setExtending(Class<?> extending) {
        this.extending = extending;
    }

    public SketchedJavaInheritance(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public SketchedJavaInheritance(Table table, String className, QObject parent, boolean isPrototype,
                                   Class<?> extending) {
        super(table, className, parent, isPrototype);
        this.extending = extending;
    }

    public SketchedJavaInheritance(Class<?> extending) {
        this(new Table(), prototype(null).className, prototype, false, extending);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new SketchedJavaInheritance(new Table(), className, this, false,
                extending);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new SketchedJavaInheritance(new Table(), className, this, true,
                extending);
    }

    @Override
    public QObject copy() {
        QObject copy = new SketchedJavaInheritance(table, className, parent, isPrototype,
                extending);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
