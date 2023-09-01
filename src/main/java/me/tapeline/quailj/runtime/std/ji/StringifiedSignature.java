package me.tapeline.quailj.runtime.std.ji;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class StringifiedSignature {

    public static StringifiedSignature parse(String signature) throws ClassNotFoundException {
        String[] parts = signature.split(" ");
        String name = parts[parts.length - 1];
        String type = parts[parts.length - 2];
        int mod = 0;
        for (int i = 0; i < parts.length - 2; i++) {
            if (parts[i].equals("public")) mod |= Modifier.PUBLIC;
            if (parts[i].equals("private")) mod |= Modifier.PRIVATE;
            if (parts[i].equals("protected")) mod |= Modifier.PROTECTED;
            if (parts[i].equals("final")) mod |= Modifier.FINAL;
            if (parts[i].equals("static")) mod |= Modifier.STATIC;
        }
        return new StringifiedSignature(ClassUtils.getClass(type), name, mod);
    }

    public static StringifiedSignature parseJavaClass(String signature) {
        String[] parts = signature.split(" ");
        String name = parts[parts.length - 1];
        int mod = 0;
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("public")) mod |= Modifier.PUBLIC;
            if (parts[i].equals("private")) mod |= Modifier.PRIVATE;
            if (parts[i].equals("protected")) mod |= Modifier.PROTECTED;
            if (parts[i].equals("final")) mod |= Modifier.FINAL;
            if (parts[i].equals("static")) mod |= Modifier.STATIC;
        }
        return new StringifiedSignature(null, name, mod);
    }

    private Class<?> type;
    private String name;
    private Integer modifier;

    public StringifiedSignature(Class<?> type, String name, Integer modifier) {
        this.type = type;
        this.name = name;
        this.modifier = modifier;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Integer getModifier() {
        return modifier;
    }

    public static Integer parseJavaModifier(String modifiers) {
        String[] parts = modifiers.split(" ");
        int mod = 0;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("public")) mod |= Modifier.PUBLIC;
            if (parts[i].equals("private")) mod |= Modifier.PRIVATE;
            if (parts[i].equals("protected")) mod |= Modifier.PROTECTED;
            if (parts[i].equals("final")) mod |= Modifier.FINAL;
            if (parts[i].equals("static")) mod |= Modifier.STATIC;
        }
        return mod;
    }

    public static List<StringifiedSignature> parseJavaArgs(List<String> javaArgs) throws ClassNotFoundException {
        List<StringifiedSignature> args = new ArrayList<>();
        for (String arg : javaArgs)
            args.add(StringifiedSignature.parse(arg));
        return args;
    }

}
