#?toc-entry Event
#?toc-entry EventManager
#?toc-entry fireEvent
#?toc-entry handleEvents
#?toc-entry addHandler
#?toc-entry removeHandler

#?html <h2><code>lang/event</code></h2>
#?html <hr>

class Event {
    string name
}

class EventManager {
    #? Does all event management

    constructor(this, num queueCapacity = 32) {
        #? Create an EventManager with certain event queue capacity
    }

    void fireEvent(this, event) {
        #? Call a event
    }

    void handleEvents(this) {
        #? Call all event handlers that should be called according to event queue
        #? Event queue will be flushed after this operation
    }

    void addHandler(this, string name, func handler) {
        #? Register handler for specific event
    }

    void removeHandler(this, string name, func handler) {
        #? Unregister handler
    }
}

defaultHandler = EventManager()
void fireEvent(event) {
    #? This function reports to the default EventManager
    #? See <a href="#EventManager::fireEvent">EventManager::fireEvent</a>
}
void handleEvents() {
    #? This function reports to the default EventManager
    #? See <a href="#EventManager::handleEvents">EventManager::handleEvents</a>
}
void addHandler(string name, func handler) {
    #? This function reports to the default EventManager
    #? See <a href="#EventManager::addHandler">EventManager::addHandler</a>
}
void removeHandler(string name, func handler) {
    #? This function reports to the default EventManager
    #? See <a href="#EventManager::removeHandler">EventManager::removeHandler</a>
}
