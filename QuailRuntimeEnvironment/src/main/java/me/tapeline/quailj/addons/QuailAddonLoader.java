package me.tapeline.quailj.addons;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class QuailAddonLoader {

    public static QuailAddon loadAddonFromJar(File file) throws
            UnresolvedAddonMainClassException, AddonLoadException {
        JarFile jarFile;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            throw new AddonLoadException(e, file);
        }
        Enumeration<JarEntry> e = jarFile.entries();
        String mainClass = null;
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();
            if (entry.getName().equals("addonMainClass")) {
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(entry)));
                } catch (IOException ex) {
                    throw new AddonLoadException(ex, file);
                }
                try {
                    String line = reader.readLine();
                    if (line == null) throw new UnresolvedAddonMainClassException(file);
                    mainClass = line.trim();
                    break;
                } catch (IOException exception) {
                    throw new UnresolvedAddonMainClassException(file);
                }
            }
        }

        URL[] urls;
        try {
            urls = new URL[]{ new URL("jar:file:" + file + "!/") };
        } catch (MalformedURLException ex) {
            throw new AddonLoadException(ex, file);
        }
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            try {
                Class<?> c = cl.loadClass(className);
                if (className.equals(mainClass)) {
                    return (QuailAddon) c.newInstance();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
                throw new AddonLoadException(exception, file);
            }
        }

        throw new UnresolvedAddonMainClassException(file);
    }

    public static QuailAddon loadAndRegisterFromJar(File file) throws UnresolvedAddonMainClassException,
            AddonLoadException, AddonAlreadyRegisteredException {
        QuailAddon addon = loadAddonFromJar(file);
        QuailAddonRegistry.registerAddon(addon);
        return addon;
    }

}
