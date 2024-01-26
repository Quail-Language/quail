use "lang/event" = event

__buf = ""
print = (args...) -> {
    for a in args
        __buf += a
}

h1 = (e) -> {
         print("1")
     }
event.addHandler("evt1", h1)
event.addHandler("evt2", (e) -> {
    print("2")
})

event.fireEvent(event.Event("evt1"))
event.fireEvent(event.Event("evt2"))
event.handleEvents()

event.removeHandler("evt1", h1)
event.fireEvent(event.Event("evt1"))
event.fireEvent(event.Event("evt2"))
event.handleEvents()

# expected:
# 122
return __buf