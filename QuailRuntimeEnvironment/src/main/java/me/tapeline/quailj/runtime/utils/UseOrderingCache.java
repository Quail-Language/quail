package me.tapeline.quailj.runtime.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;

public abstract class UseOrderingCache<T, K> {

    protected T[] cache;
    protected int ptr = 0;

    public UseOrderingCache(Class<T> clazz, int size) {
        cache = (T[]) Array.newInstance(clazz, size);
    }

    protected T usedValue(T val) {
        int index = -1;
        for (int i = 0; i < ptr; i++) {
            if (cachedObjectEquals(cache[i], val)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            if (ptr < cache.length) {
                for (int i = ptr - 1; i >= 0; i--)
                    cache[i + 1] = cache[i];
                ptr++;
            } else {
                for (int i = cache.length - 2; i >= 0; i--)
                    cache[i + 1] = cache[i];
            }
        } else if (index != 0) {
            for (int i = index - 1; i >= 0; i--)
                cache[i + 1] = cache[i];
        }
        cache[0] = val;
        return val;
    }

    public T[] getCache() {
        return cache;
    }

    public abstract @Nullable T tryToGetCachedValue(K key);
    public abstract T cacheValue(T value);
    protected abstract boolean cachedObjectEquals(T o1, T o2);

}
