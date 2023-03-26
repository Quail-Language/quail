package me.tapeline.quail.utils;

import java.util.HashMap;

public class Dict {

    public static <K, V> HashMap<K, V> make(Pair<K, V>... pairs) {
        HashMap<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs)
            map.put(pair.key, pair.value);
        return map;
    }

}
