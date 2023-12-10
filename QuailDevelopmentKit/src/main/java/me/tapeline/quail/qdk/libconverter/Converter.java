package me.tapeline.quail.qdk.libconverter;

import me.tapeline.quail.qdk.templater.TemplatedFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;

public class Converter {

    // v  Load classes
    // v  Determine scope with imports
    // v  Check for simple name collision
    // v  Determine names / template classes
    // v  Generate code
    // v  Save code

    private final String[] classesToConvert;
    private final File outputFolder;
    private final String targetPackage;
    private final String classPrefix;
    private final HashMap<Class<?>, Class<?>[]> usedClasses = new HashMap<>();

    private final HashSet<Class<?>> classesScope = new HashSet<>();
    private final List<AdapterDraft> drafts = new ArrayList<>();
    private final List<TemplatedFile> templatedFiles = new ArrayList<>();

    public Converter(String[] classesToConvert, File outputFolder, String targetPackage, String classPrefix) {
        this.classesToConvert = classesToConvert;
        this.outputFolder = outputFolder;
        this.targetPackage = targetPackage;
        this.classPrefix = classPrefix;
    }

    private AdapterDraft getDraftByClass(Class<?> cls) {
        String clsName = cls.getSimpleName();
        for (AdapterDraft draft : drafts)
            if (draft.getAdaptingClass().equals(cls))
                return draft;
        return null;
    }

    private void loadSelectedClasses() throws ClassNotFoundException {
        System.out.println("Loading classes");
        List<String> jarsToLoad = new ArrayList<>();
        for (int i = 0; i < classesToConvert.length; i++) {
            String[] parts = classesToConvert[i].split(":");
            if (parts.length == 2) {
                jarsToLoad.add(parts[0]);
                classesToConvert[i] = parts[1];
            }
        }
        URLClassLoader systemLoader = ((URLClassLoader) ClassLoader.getSystemClassLoader());
        CustomClassLoader customClassLoader = new CustomClassLoader(systemLoader.getURLs());
        jarsToLoad.forEach(jarPath -> {
            try {
                customClassLoader.addURL(new File(jarPath).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
        for (String name : classesToConvert) {
            classesScope.add(customClassLoader.loadClass(name));
            System.out.println("Loaded " + name);
        }
    }

    private void scanForImports() {
        System.out.println("Scanning for deps");
        for (Class<?> cls : classesScope) {
            Set<Class<?>> usedHere = new HashSet<>();
            for (Method method : cls.getDeclaredMethods()) {
                if (Utils.notPresentedInQuail(method.getReturnType()))
                    usedHere.add(method.getReturnType());
                for (Class<?> paramCls : method.getParameterTypes())
                    if (Utils.notPresentedInQuail(paramCls))
                        usedHere.add(paramCls.getClass());
            }
            usedClasses.put(cls, usedHere.toArray(new Class<?>[0]));
            classesScope.addAll(usedHere);
        }
    }

    private void validateSimpleNames() throws IllegalArgumentException {
        System.out.println("Validating names");
        classesScope.forEach(cls -> {
            if (classesScope.stream()
                    .filter(entry -> entry.getSimpleName().equals(cls.getSimpleName()))
                    .count() > 1)
                throw new IllegalArgumentException("Simple name " + cls.getSimpleName() + " clashes");
        });
    }

    private void prepareDrafts() {
        System.out.println("Preparing drafts");
        for (Class<?> cls : classesScope)
            drafts.add(new AdapterDraft(cls, classPrefix + cls.getSimpleName(), null));
        for (AdapterDraft draft : drafts) {
            List<String> usedDrafts = new ArrayList<>();
            if (usedClasses.containsKey(draft.getAdaptingClass()))
                for (Class<?> usedClass : usedClasses.get(draft.getAdaptingClass()))
                    usedDrafts.add(getDraftByClass(usedClass).getName());
            draft.setUsedDrafts(usedDrafts.toArray(new String[0]));
        }
    }

    private void generateCode() {
        System.out.println("Generating drafts");
        for (AdapterDraft draft : drafts) {
            System.out.println("Generating " + draft.getName());
            draft.generate(targetPackage);
            templatedFiles.addAll(draft.getResultingFiles());
            System.out.println("Successfully generated " + draft.getName());
        }
    }

    private void saveFiles() throws IOException {
        System.out.println("Saving files");
        FileUtils.deleteDirectory(outputFolder);
        outputFolder.mkdirs();

        for (TemplatedFile file : templatedFiles) {
            System.out.println("Saving " + file.getFilePackage() + "." + file.getName());
            Path targetPath = outputFolder.toPath();
            for (String part : file.getFilePackage().split("\\.")) {
                targetPath = targetPath.resolve(part);
            }
            targetPath.toFile().mkdirs();
            targetPath = targetPath.resolve(file.getName() + ".java");
            FileUtils.writeStringToFile(targetPath.toFile(), file.getCode(), "UTF-8");
        }
    }

    public void convert() throws ClassNotFoundException, IOException {
        loadSelectedClasses();
        //scanForImports();
        validateSimpleNames();
        prepareDrafts();
        generateCode();
        saveFiles();
    }

}
