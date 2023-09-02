package me.tapeline.quailj.runtime.librarymanagement;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QCircularDependencyException;
import me.tapeline.quailj.typing.classes.errors.QIOException;
import me.tapeline.quailj.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class LibraryLoader {

    private Set<String> libraryRoots = new HashSet<>();
    private HashMap<String, BuiltinLibrary> builtinLibraries = new HashMap<>();
    private static final Set<String> pathsCurrentlyLoading = new HashSet<>();

    public LibraryLoader(Set<String> libraryRoots) {
        this.libraryRoots = libraryRoots;
    }

    public QObject loadLibrary(Runtime runtime, LibraryCache registry, String name) throws RuntimeStriker {
        QObject cached = registry.getCachedLibrary(name);
        if (builtinLibraries.containsKey(name)) {
            Runtime libraryRuntime = builtinLibraries.get(name).initializeRuntime();
            if (libraryRuntime == null)
                libraryRuntime = runtime;
            QObject library = builtinLibraries.get(name).constructLibrary(libraryRuntime);
            registry.cacheLibrary(name, library);
            return library;
        }
        if (cached == null) {
            for (String pathWildcard : libraryRoots) {
                pathWildcard = pathWildcard.replaceAll("\\$cwd\\$",
                        Matcher.quoteReplacement(System.getProperty("user.dir")));
                pathWildcard = pathWildcard.replaceAll("\\$script\\$",
                        Matcher.quoteReplacement(runtime.getScriptHome().getAbsolutePath()));
                File possibleFile = new File(pathWildcard.replaceAll("\\?", name));
                if (possibleFile.exists()) {
                    if (pathsCurrentlyLoading.contains(possibleFile.getAbsolutePath())) {
                        runtime.error(new QCircularDependencyException(possibleFile.getAbsolutePath()));
                        return null;
                    }
                    pathsCurrentlyLoading.add(possibleFile.getAbsolutePath());
                    String code = null;
                    try {
                        code = runtime.getIo().readFile(possibleFile.getAbsolutePath());
                    } catch (IOException e) {
                        runtime.error(new QIOException(e.toString()));
                    }
                    QuailLauncher launcher = new QuailLauncher();
                    QObject result = launcher.launchAnonymousAndHandleErrors(code, runtime.getScriptHome(),
                            new String[] {"run", name});
                    if (result == null) {
                        runtime.error("Library " + possibleFile + " does not return (provide) anything");
                        return QObject.Val();
                    }
                    pathsCurrentlyLoading.remove(possibleFile.getAbsolutePath());
                    registry.cacheLibrary(name, result);
                    return result;
                }
            }
            runtime.error("Unable to find library " + name + "\n" +
                    "Library wasn't found neither in registry or in following directories:\n" +
                    TextUtils.collectionToString(libraryRoots, "\n") + "\n" +
                    "Please check library installation and/or correct library root dir(s) definition");
            return null;
        } else return registry.getCachedLibrary(name);
    }

    public void addBuiltinLibrary(BuiltinLibrary library) {
        builtinLibraries.put(library.id(), library);
    }

}
