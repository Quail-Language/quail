class A {
  string f
}

a = A()
a.f = "TEST"
try {
  a.f = 10
} catch ClarificationException as e {
  print(e)
}

num | string b = 5
b = "abc"
try {
  b = false
} catch ClarificationException as e {
  print(e)
}

function f(bool flag) {}

f(false)
try {
  f()
} catch ClarificationException as e {
  print(e)
}
try {
  f("abc")
} catch ClarificationException as e {
  print(e)
}