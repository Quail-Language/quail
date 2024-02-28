n = num(input())
f = [((i) -> {
        if i < 2 return i
        else return _this[-2] + _this[-1]
    })(i) through 0:n as i]
print(f)
