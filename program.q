#?toc-entry Real

class Real like Number {
    #?author Tapeline
    #?since 0.1.3
    #? An accurate implementation of a real number

    num numerator
    num denominator

    constructor(this, num numerator, num denominator) {
        this.numerator = numerator
        this.denominator = denominator
    }

    override *(this, other) {
        #? Support for arithmetic operations with Number

        if other instanceof Real
            return Real(this.numerator * other.numerator, this.denominator * other.denominator)
        else
            return Real(this.numerator * other, this.denominator)
    }
}

