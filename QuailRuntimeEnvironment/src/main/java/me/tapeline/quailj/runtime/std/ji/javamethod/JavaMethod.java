package me.tapeline.quailj.runtime.std.ji.javamethod;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.ji.Arg;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableValueException;
import me.tapeline.quailj.utils.Dict;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class JavaMethod extends QObject {

    public static JavaMethod prototype = null;
    public static JavaMethod prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new JavaMethod(
                    new Table(Dict.make(

                    )),
                    "JavaMethod",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private Method method;
    private Method[] methods;
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method[] getMethods() {
        return methods;
    }

    public String getMethodName() {
        if (methods != null)
            return methods[0].getName();
        return method.getName();
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public JavaMethod(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public JavaMethod(Table table, String className, QObject parent, boolean isPrototype,
                      Method method, Object object, Method[] methods) {
        super(table, className, parent, isPrototype);
        this.method = method;
        this.object = object;
        this.methods = methods;
    }

    public JavaMethod(Object object, Method method, Method[] methods) {
        this(new Table(), prototype(null).className, prototype, false, method,
                object, methods);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new JavaMethod(new Table(), className, this, false,
                method, object, methods);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new JavaMethod(new Table(), className, this, true,
                method, object, methods);
    }

    @Override
    public QObject copy() {
        QObject copy = new JavaMethod(table, className, parent, isPrototype,
                method, object, methods);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs) throws RuntimeStriker {
        Class<?>[] classes = Arg.getClassesFromArgs(args);
        Method foundMethod = null;
        if (methods.length > 0) {
            for (Method method : methods) {
                if (Arg.isApplicable(method.getParameterTypes(), classes)) {
                    foundMethod = method;
                    break;
                }
            }
        } else {
            foundMethod = method;
            if (!Arg.isApplicable(method.getParameterTypes(), classes)) {
                runtime.error(new QUnsuitableValueException(
                        "Method is unsuitable for provided args", this));
            }
        }
        if (foundMethod == null) {
            runtime.error(new QUnsuitableValueException(
                    "Method is unsuitable for provided args", this));
            return Val();
        }
        try {
            Object result = foundMethod.invoke(object, Arg.transformArgs(args));
            return Arg.transformBack(result);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            runtime.error(new JIJavaException(e));
        } catch (InvocationTargetException e) {
            runtime.error(new JIJavaException(((Exception) e.getCause())));
        }
        return Val();
    }

}
