package me.tapeline.quail.qdk.libconverter.std;

import me.tapeline.quail.qdk.libconverter.ValueRepresenter;
import me.tapeline.quailj.typing.classes.QObject;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ListRepresenter implements ValueRepresenter {

    @Override
    public boolean appliesTo(Type type) {
        if (!(type instanceof ParameterizedType)) return false;
        return Collection.class.isAssignableFrom(((Class<?>) ((ParameterizedType) type).getRawType()));
    }

    @Override
    public Result convertToJava(Type type, String variableName, String value) {
        String code = variableName + " = " + value + ".stream().map(obj"+hashCode() + " -> {\n" +
                "Object retObj"+hashCode() + ";\n";
        ValueRepresenter subRepresenter = ValueRepresenter.getRepresenterForType(
                ((ParameterizedType) type).getActualTypeArguments()[0]);
        if (subRepresenter == null) return new Result("UNABLE_TO_CONVERT", new Class[0]);
        Result subResult = subRepresenter.convertToJava(
                ((ParameterizedType) type).getActualTypeArguments()[0],
                "retObj"+hashCode(),
                "obj"+hashCode()
        );
        code += subResult.getResult() + "return obj"+hashCode() + ";\n" +
                "}).collect(Collectors.toList());\n";
        Class<?>[] allImports = ArrayUtils.addAll(subResult.getImports(), new Class[] {List.class});
        return new Result(
                code,
                allImports
        );
    }

    @Override
    public Result convertFromJava(Type type, String variableName, String value) {
        return new Result(
                "INSERT_CODE\n",
                new Class<?>[0]
        );
    }

}
