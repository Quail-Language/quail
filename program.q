use "lang/reflect" = reflect
use "lang/math" = math

class Real like Number {
    num n
    num d

    constructor(this, num n, num d) {
        this.n = n
        this.d = d
        print(this.n, this.d)
        this.__valueUpdated()
    }

    constructor(this, num x) {
        fraction = x % 1
        mul = 10 ^ (string(fraction).size() - 2)
        this.n = x * mul
        this.d = mul
        this.__valueUpdated()
        this.optimize()
    }

    void __valueUpdated(this) {
        reflect.setNumberValue(this, this.n / this.d)
    }

    void optimize(this) {
        m = math.gcd(this.n, this.d)
        this.n /= m
        this.d /= m
    }

    override string(this) {
        return string(this.n) + "/" + string(this.d)
    }

    override +(this, v) {
        if v instanceof Real {
            m = math.lcm(this.d, v.d)
            this.n = this.n * (m / this.d) + v.n * (m / v.d)
            this.d = m
        } else if v instanceof Number {
            this.n += v * this.d
        } else {
            throw UnsupportedOperationException() % {
                "left" = this,
                "right" = v,
                "operation" = "+"
            }
        }
        this.optimize()
        this.__valueUpdated()
        return this
    }

}

a = Real(0.5)
b = Real(1, 3)
print(a + b)