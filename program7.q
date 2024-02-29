use "lang/ji" = ji

JFrame = ji.getClass("javax.swing.JFrame")

win = JFrame("My window")
win.setSize(640, 480)
win.setDefaultCloseOperation(3)
win.setVisible(true)

while win.isVisible() {}