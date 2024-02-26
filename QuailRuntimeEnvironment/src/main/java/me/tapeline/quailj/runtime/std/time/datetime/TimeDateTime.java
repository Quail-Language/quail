package me.tapeline.quailj.runtime.std.time.datetime;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.data.set.DataLibSet;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QNotInitializedException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetYear;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetMillisecond;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetMinute;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetSecond;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeFormat;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetHour;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetMonth;
import me.tapeline.quailj.runtime.std.time.datetime.DateTimeGetDay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimeDateTime extends QObject {

    public static TimeDateTime prototype = null;
    public static TimeDateTime prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new TimeDateTime(
                    new Table(Dict.make(
                            new Pair<>("timestamp", Val(0)),
                            new Pair<>("_constructor", new DateTimeConstructor(runtime)),
                            new Pair<>("getYear", new DateTimeGetYear(runtime)),
                            new Pair<>("getMillisecond", new DateTimeGetMillisecond(runtime)),
                            new Pair<>("getMinute", new DateTimeGetMinute(runtime)),
                            new Pair<>("getSecond", new DateTimeGetSecond(runtime)),
                            new Pair<>("format", new DateTimeFormat(runtime)),
                            new Pair<>("getHour", new DateTimeGetHour(runtime)),
                            new Pair<>("getMonth", new DateTimeGetMonth(runtime)),
                            new Pair<>("getDay", new DateTimeGetDay(runtime))
                    )),
                    "DateTime",
                    QObject.superObject,
                    true
            );
        prototype.table.put("timestamp", Val(0), new int[] {ModifierConstants.NUM});
        prototype.table.put("timezone", Val("UTC"), new int[] {ModifierConstants.STR});
        return prototype;
    }

    public TimeDateTime(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public TimeDateTime() {
        super(new Table(), prototype.className, prototype, false);
    }

    public TimeDateTime(long timestamp) {
        this();
        set("timestamp", Val(timestamp));
    }

    public static TimeDateTime validate(Runtime runtime, QObject object) throws RuntimeStriker {
        if (!(object instanceof TimeDateTime))
            runtime.error(new QUnsuitableTypeException(prototype.className, object));
        return (TimeDateTime) object;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new TimeDateTime(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new TimeDateTime(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new TimeDateTime(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public long getTimestamp() {
        QObject obj = get("timestamp");
        if (obj.isNum()) return (long) obj.numValue();
        return 0;
    }

    public String getTimezone() {
        QObject obj = get("timezone");
        if (obj.isStr()) return obj.strValue();
        return "UTC";
    }

    @Override
    public QObject defaultSum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return new TimeDateTime(((TimeDateTime) other).getTimestamp() + getTimestamp());
        else if (other.isNum())
            return new TimeDateTime((long) (getTimestamp() + other.numValue()));
        return super.defaultSum(runtime, other);
    }

    @Override
    public QObject defaultSubtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return new TimeDateTime(getTimestamp() - ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return new TimeDateTime((long) (getTimestamp() - other.numValue()));
        return super.defaultSubtract(runtime, other);
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return Val(getTimestamp() == ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return Val(getTimestamp() == other.numValue());
        return super.defaultEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return Val(getTimestamp() != ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return Val(getTimestamp() != other.numValue());
        return super.defaultNotEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultGreater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return Val(getTimestamp() > ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return Val(getTimestamp() > other.numValue());
        return super.defaultGreater(runtime, other);
    }

    @Override
    public QObject defaultGreaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return Val(getTimestamp() >= ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return Val(getTimestamp() >= other.numValue());
        return super.defaultGreaterEqual(runtime, other);
    }

    @Override
    public QObject defaultLess(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return Val(getTimestamp() < ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return Val(getTimestamp() < other.numValue());
        return super.defaultLess(runtime, other);
    }

    @Override
    public QObject defaultLessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof TimeDateTime)
            return Val(getTimestamp() <= ((TimeDateTime) other).getTimestamp());
        else if (other.isNum())
            return Val(getTimestamp() <= other.numValue());
        return super.defaultLessEqual(runtime, other);
    }

    @Override
    public QObject defaultConvertToNumber(Runtime runtime) throws RuntimeStriker {
        return Val(getTimestamp());
    }

}