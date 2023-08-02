class Event {
    string name
}


class EventManager {
    constructor(this, num queueCapacity = 32) {}

    void fireEvent(this, event) {}

    void handleEvents(this) {}

    void addHandler(this, string name, func handler) {}
    void removeHandler(this, string name, func handler) {}
}

defaultHandler = EventManager()
void fireEvent(event) {}
void handleEvents() {}
void addHandler(string name, func handler) {}
void removeHandler(string name, func handler) {}
