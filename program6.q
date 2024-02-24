#:testdirective

@MyTestAnnotation
function f() {}

use "qta/mytestlib" = mytestlib

print(mytestlib.test())
