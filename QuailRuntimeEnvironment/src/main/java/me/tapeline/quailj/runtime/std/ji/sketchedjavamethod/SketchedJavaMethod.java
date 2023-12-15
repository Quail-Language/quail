package me.tapeline.quailj.runtime.std.ji.sketchedjavamethod;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.burningwave.core.classes.FunctionSourceGenerator;

public class SketchedJavaMethod extends QObject {

    public static SketchedJavaMethod prototype = null;
    public static SketchedJavaMethod prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new SketchedJavaMethod(
                    new Table(Dict.make(
                            new Pair<>("_constructor",
                                    new SketchedJavaMethodConstructor(runtime))
                    )),
                    "SketchedJavaMethod",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private FunctionSourceGenerator function;

    public FunctionSourceGenerator getFunction() {
        return function;
    }

    public void setFunction(FunctionSourceGenerator function) {
        this.function = function;
    }

    public SketchedJavaMethod(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public SketchedJavaMethod(Table table, String className, QObject parent, boolean isPrototype,
                              FunctionSourceGenerator function) {
        super(table, className, parent, isPrototype);
        this.function = function;
    }

    public SketchedJavaMethod(FunctionSourceGenerator function) {
        this(new Table(), prototype(null).className, prototype, false, function);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new SketchedJavaMethod(new Table(), className, this, false,
                function);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new SketchedJavaMethod(new Table(), className, this, true,
                function);
    }

    @Override
    public QObject copy() {
        QObject copy = new SketchedJavaMethod(table, className, parent, isPrototype,
                function);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
