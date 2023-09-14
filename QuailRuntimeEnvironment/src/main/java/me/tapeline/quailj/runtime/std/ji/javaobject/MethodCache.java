package me.tapeline.quailj.runtime.std.ji.javaobject;

import me.tapeline.quailj.runtime.std.ji.javamethod.JavaMethod;
import me.tapeline.quailj.runtime.utils.UseOrderingCache;
import org.jetbrains.annotations.Nullable;

public class MethodCache extends UseOrderingCache<JavaMethod, String> {

    public MethodCache(int size) {
        super(JavaMethod.class, size);
    }

    @Override
    public @Nullable JavaMethod tryToGetCachedValue(String key) {
        JavaMethod cached = null;
        for (JavaMethod method : cache) {
            if (method == null) continue;
            if (method.getMethodName().equals(key)) {
                cached = method;
                break;
            }
        }
        if (cached != null)
            usedValue(cached);
        return cached;
    }

    @Override
    public JavaMethod cacheValue(JavaMethod value) {
        return usedValue(value);
    }

    @Override
    protected boolean cachedObjectEquals(JavaMethod o1, JavaMethod o2) {
        return o1.getMethodName().equals(o2.getMethodName());
    }

}
