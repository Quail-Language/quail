use "lang/storage" = storage

__buf = ""
print = (args...) -> {
    for a in args
        __buf += string(a)
}

writeFile("data.json", storage.saveJson({"a" = 1, "b" = 2}))
data = storage.loadJson(readFile("data.json"))
print(data.pairs())

# expected:
# [[a, 1], [b, 2]]
return __buf