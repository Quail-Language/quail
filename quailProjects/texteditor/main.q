import "commonlibs.q"
import "swing.classes.q"
import "main.util.q"


class Application {

    object<JFrame> window
    object<JTextArea> textArea
    object<JScrollPane> scrollPane
    object<JMenuBar> menuBar
    object<JMenuItem> currentFileItem
    string currentFilePath = null

    constructor (this) {
        this.createWindow()
        this.createMenu()
        this.createUI()
    }

    method createWindow(this) {
        this.window = JFrame("Quail/Swing text editor")
        this.window.setSize(800, 600)
        this.window.setLocationRelativeTo(null)
        this.window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    }

    method createMenu(this) {
        that = this
        this.menuBar = JMenuBar()
        fileMenu = JMenu("File")

        openFile = fileMenu.add(JMenuItem("Open file"))
        openFile.addActionListener(asActionListener(function (this, event) {
            fileChooser = JFileChooser()
            result = fileChooser.showOpenDialog(null)
            if result == JFileChooser.APPROVE_OPTION {
                selectedFile = fileChooser.getSelectedFile()
                that.currentFilePath = selectedFile.getAbsolutePath()
                that.textArea.setText(readFile(that.currentFilePath))
                that.currentFileItem.setLabel(that.currentFilePath)
            }
        })())

        saveFile = fileMenu.add(JMenuItem("Save file"))
        saveFile.addActionListener(asActionListener(function (this, event) {
            if that.currentFilePath == null {
                fileChooser = JFileChooser()
                result = fileChooser.showSaveDialog(null)
                if result == JFileChooser.APPROVE_OPTION {
                    selectedFile = fileChooser.getSelectedFile()
                    that.currentFilePath = selectedFile.getAbsolutePath()
                    writeFile(that.currentFilePath, that.textArea.getText())
                    that.currentFileItem.setLabel(that.currentFilePath)
                }
            } else {
                writeFile(that.currentFilePath, that.textArea.getText())
            }
        })())

        saveFileAs = fileMenu.add(JMenuItem("Save file as..."))
        saveFileAs.addActionListener(asActionListener(function (this, event) {
            fileChooser = JFileChooser()
            result = fileChooser.showSaveDialog(null)
            if result == JFileChooser.APPROVE_OPTION {
                selectedFile = fileChooser.getSelectedFile()
                that.currentFilePath = selectedFile.getAbsolutePath()
                writeFile(that.currentFilePath, that.textArea.getText())
                that.currentFileItem.setLabel(that.currentFilePath)
            }
        })())

        closeAction = fileMenu.add(JMenuItem("Close"))
        closeAction.addActionListener(asActionListener(function (this, event) {
            that.window.dispose()
        })())

        this.currentFileItem = JMenuItem("Untitled")
        this.currentFileItem.setEnabled(false)

        this.menuBar.add(fileMenu)
        this.menuBar.add(this.currentFileItem)
        this.window.setJMenuBar(this.menuBar)
    }

    method createUI(this) {
        this.textArea = JTextArea()
        this.scrollPane = JScrollPane(this.textArea)
        this.window.add(this.scrollPane)
    }

    method run(this) {
        this.window.setVisible(true)
        while (this.window.isVisible()) {}
    }

}


function main() {
    app = Application()
    app.run()
}


main()
