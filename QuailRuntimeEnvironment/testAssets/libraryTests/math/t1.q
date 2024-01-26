use "lang/math" = math

__buf = ""
print = (args...) -> {
    for a in args
        __buf += string(a)
}

print(math.product([1, 2, 3, 4]))

# expected:
# 24
return __buf