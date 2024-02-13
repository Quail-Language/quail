use "lang/fs" = fs

__buf = ""
print = (args...) -> {
    for a in args
        __buf += string(a)
}

fs.createBlank("__test__")
fs.mkdirs("__testdir__")
fs.createBlank("__testdir__/test")
print(fs.filesIn("__testdir__"))
print(fs.fileName("__test__"))

fs.setEncoding("UTF-8")
print(fs.getEncoding())

print(num(fs.isDirectory("__testdir__")))
print(num(fs.isDirectory("__test__")))
print(num(fs.isFile("__test__")))
print(num(fs.isFile("__testdir__")))

fs.writeBinary("__test__", "dGVzdA==")
print(fs.readBinary("__test__"))

fs.delete("__testdir__")
fs.deleteFile("__test__")

# expected:
# [test]__test__UTF-81010dGVzdA==
return __buf