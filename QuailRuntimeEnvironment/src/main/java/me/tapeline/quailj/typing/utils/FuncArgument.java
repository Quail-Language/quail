package me.tapeline.quailj.typing.utils;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.TextUtils;

public class FuncArgument {

    private String name;
    private QObject defaultValue;
    private int[] modifiers;
    private int type;

    public FuncArgument(String name, QObject defaultValue, int[] modifiers, int type) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.modifiers = modifiers;
        this.type = type;
    }

    public static FuncArgument fromParsedArgument(Runtime runtime, LiteralFunction.Argument argument) throws RuntimeStriker {
        return new FuncArgument(argument.name, runtime.run(argument.defaultValue,
                runtime.getMemory()), argument.modifiers, argument.type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QObject getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(QObject defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(int[] modifiers) {
        this.modifiers = modifiers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return TextUtils.modifiersToStringRepr(modifiers) + " " + name;
    }

}
