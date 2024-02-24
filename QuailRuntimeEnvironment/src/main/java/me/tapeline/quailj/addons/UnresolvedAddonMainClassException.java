package me.tapeline.quailj.addons;

import java.io.File;

public class UnresolvedAddonMainClassException extends Exception {

    protected File file;

    public UnresolvedAddonMainClassException(File file) {
        super("Cannot resolve main class in addon " + file);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
