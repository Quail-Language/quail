use "lang/event" = event

mgr = event.EventManager()

__buf = ""
print = (args...) -> {
    for a in args
        __buf += a
}

h1 = (e) -> {
         print("1")
     }
mgr.addHandler("evt1", h1)
mgr.addHandler("evt2", (e) -> {
    print("2")
})

mgr.fireEvent(event.Event("evt1"))
mgr.fireEvent(event.Event("evt2"))
mgr.handleEvents()

mgr.removeHandler("evt1", h1)
mgr.fireEvent(event.Event("evt1"))
mgr.fireEvent(event.Event("evt2"))
mgr.handleEvents()

# expected:
# 122
return __buf