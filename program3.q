use "lang/ji" = ji

_Pair = ji.SketchedJavaClass("public NumPair",
    ji.SketchedJavaField("private double a"),
    ji.SketchedJavaField("private double b"),
    ji.SketchedJavaConstructor("public", ["double a", "double b"], function (this, a, b) {
        this.a = a
        this.b = b
    }),
    ji.SketchedJavaMethod("public double getA", [], function (this) {
        return this.a
    }),
    ji.SketchedJavaMethod("public double getB", [], function (this) {
        return this.b
    })
)

pkg = ji.deployPackage(
    ji.SketchedJavaPackage("me.tapeline.quailj.ji.test",
        _Pair
    ),
    "program3_q/JIGenerated",
    ["me.tapeline.quailj.ji.test.NumPair"]
 )

Pair = pkg[0]
pair = Pair(3, 6)
print(pair.getA())
print(pair.getB())
