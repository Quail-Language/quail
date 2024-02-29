use "lang/qml" = qml
 
win = qml.Window("Graph")
win.setSize(600, 600)
win.centerOnScreen()
 
surface = qml.Surface(600, 600)

# Calculate graph
f = sin
coordR = 4
function coord(x)
  return (x + coordR) * 600 / (coordR * 2)
 
stepX = 0.1
dataX = -coordR:+coordR:stepX
dataY = map(f, dataX)

# Draw coordinates
surface.setColor([0, 0, 255])
surface.drawLine(0, 300, 600, 300)
surface.drawLine(300, 0, 300, 600)
surface.setColor([255, 0, 0])
through 0:-coordR as x
  surface.drawText(coord(x), 300, string(x))
through 0:coordR as x
  surface.drawText(coord(x), 300, string(x))
through 0:-coordR as y
  surface.drawText(300, coord(y), string(y))
through 0:coordR as y
  surface.drawText(300, coord(y), string(y))
surface.drawText(320, 20, "y")
surface.drawText(560, 310, "x")

# Draw graph
surface.setColor([0, 255, 0])
through 0:(dataX.size()-1) as i {
  surface.drawLine(
    coord(dataX[i]),
    coord(dataY[i]),
    coord(dataX[i+1]),
    coord(dataY[i+1])
  )
}
 
win.draw(surface)
win.setVisible(true)
 
while win.isVisible() {}