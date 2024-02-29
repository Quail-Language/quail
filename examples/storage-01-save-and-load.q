use "lang/storage" = storage

writeFile("data.yml", storage.saveYaml({"a" = 1, "b" = 2}))

data = storage.loadYaml(readFile("data.yml"))

print(data.pairs())
