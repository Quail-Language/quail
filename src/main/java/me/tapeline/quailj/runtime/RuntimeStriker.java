package me.tapeline.quailj.runtime;

import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QException;

public class RuntimeStriker extends Exception {

    public enum Type {
        BREAK, RETURN, CONTINUE, EXCEPTION, EXIT;
    }

    private QObject carryingError;
    private QObject carryingReturnValue;
    private int strikePower;
    private int exitCode;
    private Type type;


    public RuntimeStriker(Type type, QObject data) {
        super(type.name() + data.toString());
        this.type = type;
        if (type == Type.EXCEPTION) carryingError = data;
        else if (type == Type.RETURN) carryingReturnValue = data;
    }

    public RuntimeStriker(Type type, int data) {
        super(type.name() + data);
        this.type = type;
        if (type == Type.BREAK) strikePower = data;
        else if (type == Type.EXIT) exitCode = data;
    }

    public RuntimeStriker(String message) {
        super(message);
        this.type = Type.EXCEPTION;
        this.carryingError = new QException(message);
    }

    public QObject getCarryingError() {
        return carryingError;
    }

    public QObject getCarryingReturnValue() {
        return carryingReturnValue;
    }

    public int getStrikePower() {
        return strikePower;
    }

    public Type getType() {
        return type;
    }

    public int getExitCode() {
        return exitCode;
    }

}
