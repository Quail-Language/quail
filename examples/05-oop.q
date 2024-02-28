LetterType = {
  "VOWEL": 0,
  "CONSONANT": 1
}

class Letter {
  string small
  string capital
  num type
  
  constructor (this, type, capital, small) {
    this.type = type
    this.capital = capital
    this.small = small
  }
  
  override string(this) {
    return "Letter " + this.capital
  }
}

class Vowel like Letter {
  constructor (this, capital, small) {
    Letter._constructor(this, LetterType.VOWEL, capital, small)
  }
}

class Consonant like Letter {
  constructor (this, capital, small) {
    Letter._constructor(this, LetterType.CONSONANT, capital, small)
  }
}

class LetterA like Vowel {
  constructor (this) {
    Vowel._constructor(this, "A", "a")
  }
}

a = LetterA()
print(string(a))