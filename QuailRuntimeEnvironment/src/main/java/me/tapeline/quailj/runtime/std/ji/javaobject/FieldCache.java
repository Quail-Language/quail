package me.tapeline.quailj.runtime.std.ji.javaobject;

import me.tapeline.quailj.runtime.utils.UseOrderingCache;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;

public class FieldCache extends UseOrderingCache<Field, String> {

    public FieldCache(int size) {
        super(Field.class, size);
    }

    @Override
    public @Nullable Field tryToGetCachedValue(String key) {
        Field cached = null;
        for (Field field : cache) {
            if (field == null) continue;
            if (field.getName().equals(key)) {
                cached = field;
                break;
            }
        }
        if (cached != null)
            usedValue(cached);
        return cached;
    }

    @Override
    public Field cacheValue(Field value) {
        return usedValue(value);
    }

    @Override
    protected boolean cachedObjectEquals(Field o1, Field o2) {
        return o1.getName().equals(o2.getName());
    }

}
