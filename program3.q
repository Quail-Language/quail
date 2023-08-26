Pair = ji.SketchedJavaClass("public NumPair",
    ji.SketchedJavaField("private double a"),
    ji.SketchedJavaField("private double b"),
    ji.SketchedJavaConstructor([["double", "a"], ["double", "b"]], function (this, a, b) {
        this.a = a
        this.b = b
    }),
    ji.SketchedJavaMethod("public double getA", [], function (this) {
        return this.a
    }),
    ji.SketchedJavaMethod("public double getB", [], function (this) {
        return this.b
    }),
)

ji.deployPackage(ji.SketchedJavaPackage("me.tapeline.quailj.ji.test", Pair))
