package me.tapeline.quailj.runtime.librarymanagement;

import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class LibraryCache {

    private final HashMap<String, QObject> cachedLibraries = new HashMap<>();
    public LibraryCache() {}

    public void cacheLibrary(String name, QObject library) {
        cachedLibraries.put(name, library);
    }

    public QObject getCachedLibrary(String name) {
        return cachedLibraries.get(name);
    }

}
