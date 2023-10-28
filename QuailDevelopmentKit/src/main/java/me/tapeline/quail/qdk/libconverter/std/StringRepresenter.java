package me.tapeline.quail.qdk.libconverter.std;

import me.tapeline.quail.qdk.libconverter.ValueRepresenter;

import java.lang.reflect.Type;
import java.util.Objects;

public class StringRepresenter implements ValueRepresenter {

    @Override
    public boolean appliesTo(Type type) {
        if (!(type instanceof Class<?>)) return false;
        return type.equals(String.class);
    }

    @Override
    public Result convertToJava(Type type, String variableName, String value) {
        return new Result(
                variableName + " = Objects.requireNonNull(" + value + ".strValue());\n",
                new Class<?>[] {
                        Objects.class
                }
        );
    }

    @Override
    public Result convertFromJava(Type type, String variableName, String value) {
        return new Result(
                variableName + " = QObject.Val(" + value + ");\n",
                new Class<?>[0]
        );
    }

}
