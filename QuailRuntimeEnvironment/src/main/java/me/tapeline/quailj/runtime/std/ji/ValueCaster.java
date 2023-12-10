package me.tapeline.quailj.runtime.std.ji;

public class ValueCaster<T> {

    private final Object value;

    public ValueCaster(Object value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public T cast(Class<? extends T> to) {
        if (value instanceof Number) {
            if (to == Integer.class)
                return (T) ((Integer) ((Number) value).intValue());
            if (to == Byte.class)
                return (T) ((Byte) ((Number) value).byteValue());
            if (to == Short.class)
                return (T) ((Short) ((Number) value).shortValue());
            if (to == Float.class)
                return (T) ((Float) ((Number) value).floatValue());
            if (to == Double.class)
                return (T) ((Double) ((Number) value).doubleValue());
            if (to == Long.class)
                return (T) ((Long) ((Number) value).longValue());
        }
        return (T) value;
    }

}
