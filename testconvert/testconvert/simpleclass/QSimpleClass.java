package testconvert.simpleclass;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QSimpleClass extends QObject {

    public static QSimpleClass prototype = null;
    public static QSimpleClass prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new Event(
                    new Table(Dict.make(
                            new Pair<>("aMethod", new QSimpleClassFuncAMethod(runtime)),
                    )),
                    "QSimpleClass",
                    FILL_THIS_IN,
                    true
            );
        return prototype;
    }

    public QSimpleClass(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QSimpleClass(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QSimpleClass(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QSimpleClass(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
