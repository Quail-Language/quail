package me.tapeline.quailj.test.runtime.caching;

import me.tapeline.quailj.runtime.std.ji.javaobject.FieldCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheTests {

    public static class A {
        public Object a;
        public Object b;
        public Object c;
        public Object d;
    }

    @Test
    public void fieldCacheTest() throws NoSuchFieldException {
        FieldCache cache = new FieldCache(3);
        cache.cacheValue(A.class.getField("a"));
        cache.cacheValue(A.class.getField("b"));
        cache.cacheValue(A.class.getField("c"));
        cache.cacheValue(A.class.getField("d"));
        Assertions.assertTrue(
                cache.getCache()[0].getName().equals("d") &&
                cache.getCache()[1].getName().equals("c") &&
                cache.getCache()[2].getName().equals("b")
        );
        cache.cacheValue(A.class.getField("a"));
        Assertions.assertTrue(
                cache.getCache()[0].getName().equals("a") &&
                        cache.getCache()[1].getName().equals("d") &&
                        cache.getCache()[2].getName().equals("c")
        );
        cache.cacheValue(A.class.getField("d"));
        Assertions.assertTrue(
                cache.getCache()[0].getName().equals("d") &&
                        cache.getCache()[1].getName().equals("a") &&
                        cache.getCache()[2].getName().equals("c")
        );
        cache.cacheValue(A.class.getField("c"));
        Assertions.assertTrue(
                cache.getCache()[0].getName().equals("c") &&
                        cache.getCache()[1].getName().equals("d") &&
                        cache.getCache()[2].getName().equals("a")
        );
    }

}
