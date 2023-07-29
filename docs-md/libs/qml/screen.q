class WindowNotInitializedException {}
class SurfaceNotInitializedException {}

class Window {
    constructor(string title) {}

    void setTitle(string title) {}
    string getTitle() {}

    void setVisible(bool isVisible) {}
    bool isVisible() {}
    bool isFocused() {}
    void requestFocus() {}

    void setSize(num width, num height) {}
    num getWidth() {}
    num getHeight() {}
    void setResizable(bool isResizable) {}
    bool isResizable() {}
    void setFullscreen(bool isFullscreen) {}
    bool isFullscreen() {}

    void setPosition(num x, num y) {}
    num getPositionX() {}
    num getPositionY() {}
    void centerOnScreen() {}

    void draw(surface) {}

    num mouseX() {}
    num mouseY() {}
    num mouseAbsX() {}
    num mouseAbsY() {}
    num mouseButton() {}
    bool mousePressed() {}
}


class Keyboard {
    static bool anyPressed() {}
    static list getPressed() {}
    static num lastPressedCode() {}
    static string lastPressedChar() {}
    static bool ctrlPressed() {}
    static bool altPressed() {}
    static bool shiftPressed() {}
}


class Surface {
    constructor(num w, num h) {}

    num getWidth() {}
    num getHeight() {}

    void setFont(font) {}
    object getFont() {}

    void setColor(list color) {}
    void setColorRGBA(list color) {}
    void setColorHSB(list color) {}
    list getColor() {}
    list getColorRGBA() {}
    list getColorHSB() {}

    void clear() {}
    void drawPixel(num x, num y) {}
    void drawLine(num x1, num y1, num x2, num y2) {}
    void drawRect(num x, num y, num w, num h) {}
    void fillRect(num x, num y, num w, num h) {}
    void drawOval(num x, num y, num w, num h) {}
    void fillOval(num x, num y, num w, num h) {}
    void drawPoly(list points) {}
    void fillPoly(list points) {}
    void drawText(num x, num y, string text) {}
    void stamp(num x, num y, surface) {}
}


class Font {
    STYLE = {
        "PLAIN" = 0,
        "BOLD" = 1,
        "ITALIC" = 2,
        "BOLD_ITALIC" = 3
    }

    constructor(string name, num style, num size) {}

    string getName() {}
    num getStyle() {}
    num getSize() {}

    list getMetricsFor(string text) {}
}