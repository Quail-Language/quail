package me.tapeline.quailj.addons;

import java.io.File;

public class AddonLoadException extends Exception {

    protected File file;

    public AddonLoadException(String message, File file) {
        super(message + "\nFile: " + file);
        this.file = file;
    }

    public AddonLoadException(Throwable cause, File file) {
        super("Cannot load addon " + file + " due to " + cause, cause);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
