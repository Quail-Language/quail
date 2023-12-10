use "lang/qml" = qml

class Application {

    list windows
    string name
    num fps

    constructor(this, string name) {
        this.windows = []
        this.name = name
        this.fps = 30
    }

    void run(this) {
        print("Application " + this.name + " started")
        timeFrame = 1000 / this.fps
        while (win.isVisible()) {
            frameStart = millis()
            map(function (win) win.getEventManager().handleEvents(), this.windows)
            while (millis() < frameStart + timeFrame) {}
        }
        print("Application " + this.name + " ended")
    }

}


class Component {
    num fixedW
    num fixedH
    num horizontalWeight
    num verticalWeight
    bool isFixedW
    bool isFixedH
    func renderer

    void draw() {}
}


class Panel like Component {
    func layout


}