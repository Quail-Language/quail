use "lang/qml" = qml

img = qml.Surface(300, 300)
img.setColor([255, 0, 0])
img.fillRect(0, 0, 50, 50)

win = qml.Window("1")
win.setSize(300, 300)
win.centerOnScreen()
win.setVisible(true)

win.draw(img)

function clickHandler(event)
    print(event.x, event.y, event.button, event.clickCount)

win.getEventManager().addHandler("qml.mouseClick", clickHandler)

fps = 30
timeFrame = 1000 / fps

while (win.isVisible()) {
    frameStart = millis()
    win.getEventManager().handleEvents()
    while (millis() < frameStart + timeFrame) {}
}