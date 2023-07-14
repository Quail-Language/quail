package me.tapeline.quailj.runtime;

import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QException;
import me.tapeline.quailj.utils.PositionedException;

public class RuntimeStriker extends PositionedException {

    public enum Type {
        BREAK, RETURN, CONTINUE, EXCEPTION, EXIT;
    }

    private QObject carryingError;
    private QObject carryingReturnValue;
    private int strikePower;
    private int exitCode;
    private Type type;

    public RuntimeStriker(Type type, QObject data, int character, int line, int length) {
        super(type.name() + data.toString(), character, line, length);
        this.type = type;
        if (type == Type.EXCEPTION) carryingError = data;
        else if (type == Type.RETURN) carryingReturnValue = data;
    }

    public RuntimeStriker(Type type, int data, int character, int line, int length) {
        super(type.name() + data, character, line, length);
        this.type = type;
        if (type == Type.BREAK) strikePower = data;
        else if (type == Type.EXIT) exitCode = data;
    }

    public RuntimeStriker(String message, int character, int line, int length) {
        super(message, character, line, length);
        this.type = Type.EXCEPTION;
        this.carryingError = new QException(message);
    }

    public RuntimeStriker(Type type, int character, int line, int length) {
        super(type.name(), character, line, length);
        this.type = type;
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

    public String formatError(String code) {
        String[] lines = code.split("\n");
        StringBuilder sb = new StringBuilder();
        sb.append("At character ").append(getCharacter()).append(" line ").append(getLine()).append("\n");
        String linePrefix = " " + getLine() + " | ";
        sb.append(linePrefix);
        if (lines.length <= getLine() - 1)
            sb.append("!!! internal error !!! unable to display line !!!");
        else
            sb.append(lines[getLine() - 1]);
        sb.append("\n");
        for (int i = 0; i < linePrefix.length() + getCharacter(); i++)
            sb.append(" ");
        for (int i = 0; i < getLength(); i++)
            if (i == 0)
                sb.append("^");
            else
                sb.append("~");
        sb.append("\n");
        sb.append(carryingError.getClassName()).append(":\n");
        sb.append(carryingError.get("message").toString());
        return sb.toString();
    }

}
