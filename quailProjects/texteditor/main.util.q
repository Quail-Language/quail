function asActionListener(func f) {
    javaClass = ji.SketchedJavaClass("public QuailSwingActionListener" + string(hash(f)),
        ji.SketchedJavaInheritance(ji.getClass("java.awt.event.ActionListener")),
        ji.SketchedJavaMethod("public void actionPerformed", ["java.awt.event.ActionEvent e"], f)
    )
    pkg = ji.deployPackage(
        ji.SketchedJavaPackage(
            "me.tapeline.quailj.swingintegration.func" + string(hash(f)),
            javaClass
        ),
        fs.absolutePath("JIGenerated"),
        ["me.tapeline.quailj.swingintegration.func" + string(hash(f)) + ".QuailSwingActionListener" + string(hash(f))]
    )
    return pkg[0]
}
