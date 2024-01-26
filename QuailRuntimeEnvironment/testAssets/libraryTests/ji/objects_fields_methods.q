use "lang/ji" = ji

__buf = ""
print = (args...) -> {
    for a in args
        __buf += string(a)
}

function __dummy() {}
__runtime = ji.JavaObject.pack(__dummy).boundRuntime

obj = ji.JavaObject.pack("This is my string")

print(obj.value)
print(obj.defaultSum(__runtime, ji.JavaObject.pack("2")))

# expected:
# This is my stringThis is my string2
return __buf