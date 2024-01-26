use "lang/fs" = fs

__buf = ""
print = (args...) -> {
    for a in args
        __buf += string(a)
}

fs.createBlank("__test__")
fs.mkdirs("__testdir__")
fs.mkdirs("__testdir2__")
fs.createBlank("__testdir__/test")
print(num(fs.exists("__test__")), num(fs.exists("__testdir__/test")), num(fs.exists("__testdir2__")))

fs.delete("__testdir__")
fs.deleteFile("__test__")
fs.deleteDirectory("__testdir2__")
print(num(fs.exists("__test__")), num(fs.exists("__testdir__/test")), num(fs.exists("__testdir2__")))

# expected:
# 111000
return __buf