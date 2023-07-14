class MyException like Exception {
    constructor(this, string message) {
        this.message = message
    }
}

class Real like Number {
    num numerator
    num denominator

    constructor(this, num numerator, num denominator) {
        this.numerator = numerator
        this.denominator = denominator
    }

    override *(this, other) {
        if other instanceof Real
            return Real(this.numerator * other.numerator, this.denominator * other.denominator)
        else
            return Real(this.numerator * other, this.denominator)
    }
}

r1 =