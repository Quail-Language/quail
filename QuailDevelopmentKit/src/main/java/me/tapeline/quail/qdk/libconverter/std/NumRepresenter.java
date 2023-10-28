package me.tapeline.quail.qdk.libconverter.std;

import me.tapeline.quail.qdk.libconverter.ValueRepresenter;

import java.lang.reflect.Type;
import java.util.Objects;

public class NumRepresenter implements ValueRepresenter {

    @Override
    public boolean appliesTo(Type type) {
        if (!(type instanceof Class<?>)) return false;
        return type.equals(Number.class) ||
                type.equals(double.class) ||
                type.equals(float.class) ||
                type.equals(short.class) ||
                type.equals(long.class) ||
                type.equals(byte.class) ||
                type.equals(int.class);
    }

    @Override
    public Result convertToJava(Type type, String variableName, String value) {
        return new Result(
                variableName + " = (" + ((Class<?>) type).getSimpleName() +
                        ") Objects.requireNonNull(" + value + ".numValue());\n",
                new Class<?>[] {
                        Objects.class
                }
        );
    }

    @Override
    public Result convertFromJava(Type type, String variableName, String value) {
        return new Result(
                variableName + " = QObject.Val((double) " + value + ");\n",
                new Class<?>[0]
        );
    }

}
