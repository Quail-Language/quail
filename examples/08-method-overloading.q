function len(string s) {
  return s.size()
}

function len(list l) {
  return l.size()
}

function len(num n) {
  return n
}

function len(bool b) {
  return num(b)
}

print(len([1, 2, 3]))
print(len("abcdef"))
print(len(2))
print(len(true))