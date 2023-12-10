package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class FSLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/fs";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("absolutePath", new FsAbsolutePath(runtime));
        contents.put("createBlank", new FsCreateBlank(runtime));
        contents.put("delete", new FsDelete(runtime));
        contents.put("deleteDirectory", new FsDeleteDirectory(runtime));
        contents.put("deleteFile", new FsDeleteFile(runtime));
        contents.put("exists", new FsExists(runtime));
        contents.put("fileName", new FsFileName(runtime));
        contents.put("filesIn", new FsFilesIn(runtime));
        contents.put("getEncoding", new FsGetEncoding(runtime));
        contents.put("isDirectory", new FsIsDirectory(runtime));
        contents.put("isFile", new FsIsFile(runtime));
        contents.put("makeReadOnly", new FsMakeReadOnly(runtime));
        contents.put("mkdirs", new FsMkdirs(runtime));
        contents.put("readBinary", new FsReadBinary(runtime));
        contents.put("setEncoding", new FsSetEncoding(runtime));
        contents.put("writeBinary", new FsWriteBinary(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
