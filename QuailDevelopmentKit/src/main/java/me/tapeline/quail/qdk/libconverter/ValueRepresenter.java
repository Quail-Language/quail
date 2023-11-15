package me.tapeline.quail.qdk.libconverter;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ValueRepresenter {

    class Result {
        private String result;
        private Class<?>[] imports;

        public Result(String result, Class<?>[] imports) {
            this.result = result;
            this.imports = imports;
        }

        public String getResult() {
            return result;
        }
        public Class<?>[] getImports() {
            return imports;
        }
    }

    boolean appliesTo(Type type);
    Result convertToJava(Type type, String variableName, String value);
    Result convertFromJava(Type type, String variableName, String value);

    static List<ValueRepresenter> loadedRepresenters = new ArrayList<>();
    static ValueRepresenter getRepresenterForType(Type type) {
        if (loadedRepresenters.isEmpty()) {
            Reflections reflections = new Reflections();
            Set<Class<? extends ValueRepresenter>> subTypes = reflections.getSubTypesOf(ValueRepresenter.class);
            for (Class<?> subType : subTypes) {
                try {
                    ValueRepresenter representer = (ValueRepresenter) subType.getConstructor().newInstance();
                    loadedRepresenters.add(representer);
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                         IllegalAccessException ignored) {
                }
            }
        }
        for (ValueRepresenter representer : loadedRepresenters)
            if (representer.appliesTo(type))
                return representer;
        return null;
    }

}
