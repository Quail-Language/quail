use "lang/storage" = storage

__buf = ""
print = (args...) -> {
    for a in args
        __buf += string(a)
}

writeFile("data.yml", storage.saveYaml({"a" = 1, "b" = 2}))
data = storage.loadYaml(readFile("data.yml"))
print(data.pairs())

# expected:
# [[a, 1], [b, 2]]
return __buf