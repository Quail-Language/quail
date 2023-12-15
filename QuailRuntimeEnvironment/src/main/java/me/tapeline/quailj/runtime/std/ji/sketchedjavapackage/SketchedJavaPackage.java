package me.tapeline.quailj.runtime.std.ji.sketchedjavapackage;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.burningwave.core.classes.UnitSourceGenerator;


public class SketchedJavaPackage extends QObject {

    public static SketchedJavaPackage prototype = null;
    public static SketchedJavaPackage prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new SketchedJavaPackage(
                    new Table(Dict.make(
                            new Pair<>("_constructor",
                                    new SketchedJavaPackageConstructor(runtime))
                    )),
                    "SketchedJavaPackage",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private UnitSourceGenerator unit;

    public UnitSourceGenerator getUnit() {
        return unit;
    }

    public void setUnit(UnitSourceGenerator unit) {
        this.unit = unit;
    }

    public SketchedJavaPackage(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public SketchedJavaPackage(Table table, String className, QObject parent, boolean isPrototype,
                               UnitSourceGenerator unit) {
        super(table, className, parent, isPrototype);
        this.unit = unit;
    }

    public SketchedJavaPackage(UnitSourceGenerator unit) {
        this(new Table(), prototype(null).className, prototype, false, unit);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new SketchedJavaPackage(new Table(), className, this, false,
                unit);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new SketchedJavaPackage(new Table(), className, this, true,
                unit);
    }

    @Override
    public QObject copy() {
        QObject copy = new SketchedJavaPackage(table, className, parent, isPrototype,
                unit);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
