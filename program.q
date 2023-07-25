#?toc-entry Real

#?html <h1>Number utilities</h1><hr>

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

    string toString(this) {
        #? Returns string representation like this: 1/3
        #?since 0.3
        return string(this.numerator) + "/" + string(this.denominator)
    }

}
