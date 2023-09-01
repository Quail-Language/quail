package me.tapeline.quailj.runtime.std.ji.sketchedjavafield;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.SourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;


public class SketchedJavaField extends QObject {

    public static SketchedJavaField prototype = null;
    public static SketchedJavaField prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new SketchedJavaField(
                    new Table(Dict.make(
                            new Pair<>("_constructor",
                                    new SketchedJavaFieldConstructor(runtime))
                    )),
                    "SketchedJavaField",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private VariableSourceGenerator variable;

    public VariableSourceGenerator getVariable() {
        return variable;
    }

    public void setVariable(VariableSourceGenerator variable) {
        this.variable = variable;
    }

    public SketchedJavaField(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public SketchedJavaField(Table table, String className, QObject parent, boolean isPrototype,
                             VariableSourceGenerator variable) {
        super(table, className, parent, isPrototype);
        this.variable = variable;
    }

    public SketchedJavaField(VariableSourceGenerator variable) {
        this(new Table(), prototype(null).className, prototype, false, variable);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new SketchedJavaField(new Table(), className, this, false,
                variable);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new SketchedJavaField(new Table(), className, this, true,
                variable);
    }

    @Override
    public QObject copy() {
        QObject copy = new SketchedJavaField(table, className, parent, isPrototype,
                variable);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
