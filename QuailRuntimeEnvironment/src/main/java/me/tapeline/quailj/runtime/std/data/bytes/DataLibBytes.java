package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.basic.threading.QThread;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.*;
import me.tapeline.quailj.typing.classes.utils.Initializable;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.runtime.std.data.bytes.BytesXnor;
import me.tapeline.quailj.runtime.std.data.bytes.BytesOr;
import me.tapeline.quailj.runtime.std.data.bytes.BytesAsSignedInt;
import me.tapeline.quailj.runtime.std.data.bytes.BytesAsBase64;
import me.tapeline.quailj.runtime.std.data.bytes.BytesFromBase64;
import me.tapeline.quailj.runtime.std.data.bytes.BytesNor;
import me.tapeline.quailj.runtime.std.data.bytes.BytesFromSignedInt;
import me.tapeline.quailj.runtime.std.data.bytes.BytesAsUnsignedInt;
import me.tapeline.quailj.runtime.std.data.bytes.BytesAnd;
import me.tapeline.quailj.runtime.std.data.bytes.BytesBitAt;
import me.tapeline.quailj.runtime.std.data.bytes.BytesXor;
import me.tapeline.quailj.runtime.std.data.bytes.BytesNand;
import me.tapeline.quailj.runtime.std.data.bytes.BytesImplies;
import me.tapeline.quailj.runtime.std.data.bytes.BytesSetBitAt;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataLibBytes extends QObject implements Initializable {

    public static DataLibBytes prototype = null;
    public static DataLibBytes prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new DataLibBytes(
                    new Table(Dict.make(
                            new Pair<>("xnor", new BytesXnor(runtime)),
                            new Pair<>("or", new BytesOr(runtime)),
                            new Pair<>("asSignedInt", new BytesAsSignedInt(runtime)),
                            new Pair<>("asBase64", new BytesAsBase64(runtime)),
                            new Pair<>("asBits", new BytesAsBits(runtime)),
                            new Pair<>("fromBase64", new BytesFromBase64(runtime)),
                            new Pair<>("fromBits", new BytesFromBits(runtime)),
                            new Pair<>("nor", new BytesNor(runtime)),
                            new Pair<>("fromSignedInt", new BytesFromSignedInt(runtime)),
                            new Pair<>("asUnsignedInt", new BytesAsUnsignedInt(runtime)),
                            new Pair<>("and", new BytesAnd(runtime)),
                            new Pair<>("bitAt", new BytesBitAt(runtime)),
                            new Pair<>("xor", new BytesXor(runtime)),
                            new Pair<>("nand", new BytesNand(runtime)),
                            new Pair<>("implies", new BytesImplies(runtime)),
                            new Pair<>("setBitAt", new BytesSetBitAt(runtime))
                    )),
                    "Bytes",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected byte[] data;
    protected int iterator;

    public DataLibBytes(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public DataLibBytes(Table table, String className, QObject parent, boolean isPrototype, byte[] data) {
        this(table, className, parent, isPrototype);
        this.data = data;
    }

    public DataLibBytes(byte[] data) {
        this(new Table(), prototype.className, prototype, false, data);
    }

    public static DataLibBytes validate(Runtime runtime, QObject object) throws RuntimeStriker {
        if (!(object instanceof DataLibBytes))
            runtime.error(new QUnsuitableTypeException(prototype.className, object));
        DataLibBytes thisObject = (DataLibBytes) object;
        if (!thisObject.isInitialized())
            runtime.error(new QNotInitializedException("Bytes"));
        return thisObject;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibBytes(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibBytes(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new DataLibBytes(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public boolean isInitialized() {
        return data != null;
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof DataLibBytes)
            return Val(Arrays.equals(((DataLibBytes) other).data, data));
        return super.defaultEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof DataLibBytes)
            return Val(!Arrays.equals(((DataLibBytes) other).data, data));
        return super.defaultNotEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultIndex(Runtime runtime, QObject index) throws RuntimeStriker {
        if (index.isNum()) {
            if (index.numValue() >= data.length) {
                runtime.error(new QIndexOutOfBoundsException(index, Val(data.length)));
                return Val();
            }
            return new DataLibBytes(new byte[] {data[((int) index.numValue())]});
        }
        return super.defaultIndex(runtime, index);
    }

    @Override
    public QObject defaultIndexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (!(value instanceof DataLibBytes)) return super.defaultIndexSet(runtime, index, value);
        byte[] valueData = ((DataLibBytes) value).data;
        if (index.isNum()) {
            if (index.numValue() >= data.length) {
                runtime.error(new QIndexOutOfBoundsException(index, Val(data.length)));
                return Val();
            }
            int caret = (int) index.numValue();
            for (int i = 0; i < valueData.length; i++) {
                if (i + caret >= data.length) break;
                data[i + caret] = valueData[i];
            }
            return Val();
        }
        return super.defaultIndexSet(runtime, index, value);
    }

    @Override
    public QObject defaultSubscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (start.isNum() && end.isNum()) {
            int startIndex = ((int) start.numValue());
            int endIndex = ((int) end.numValue());
            if (startIndex >= endIndex) {
                runtime.error(new QUnsuitableValueException("End should be bigger than start", end));
                return Val();
            }
            if (startIndex >= data.length) {
                runtime.error(new QIndexOutOfBoundsException(start, Val(data.length)));
                return Val();
            }
            if (endIndex >= data.length) {
                runtime.error(new QIndexOutOfBoundsException(end, Val(data.length)));
                return Val();
            }
            byte[] newData = new byte[endIndex - startIndex];
            System.arraycopy(data, startIndex, newData, 0, newData.length);
            return new DataLibBytes(newData);
        }
        return super.defaultSubscriptStartEnd(runtime, start, end);
    }

    @Override
    public QObject defaultIterateStart(Runtime runtime) throws RuntimeStriker {
        iterator = 0;
        return this;
    }

    @Override
    public QObject defaultIterateNext(Runtime runtime) throws RuntimeStriker {
        if (iterator >= data.length)
            runtime.error(new QIterationStopException());
        return new DataLibBytes(new byte[] {data[iterator++]});
    }
}