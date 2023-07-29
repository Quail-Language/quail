class Event {
    string name

    constructor(this, string name) {
        this.name = name
    }
}


class EventManager {
    list eventQueue
    dict eventHandlers

    constructor(this) {
        this.eventQueue = []
        this.eventHandlers = {}
    }

    void fireEvent(this, event) {
        this.eventQueue.add(event)
    }

    void handleEvents(this) {
        for event in this.eventQueue {
            if this.eventHandlers.containsKey(event.name)
                for handler in this.eventHandlers.get(event.name) {
                    result = this.eventHandlers.get(event.name)(event)
                    if (result == false) break
                }
        }
    }

    void addHandler(this, string name, func handler) {
        f = {}
        if this.eventHandlers.containsKey(name)
            this.eventHandlers.get(name).add(handler)
        else
            this.eventHandlers.set(name, [handler])
    }
}


manager = EventManager()

function handler1(event) {
    print("Event " + event.name + " fired!")
}
manager.addHandler("testEvent", handler1)

manager.fireEvent(Event("testEvent"))

manager.handleEvents()