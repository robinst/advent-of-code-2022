enum Day06 {
    static func solve1(_ input: String) throws -> Int {
        return calculate(input, length: 4)
    }

    static func solve2(_ input: String) throws -> Int {
        return calculate(input, length: 14)
    }

    static func calculate(_ input: String, length: Int) -> Int {
        let chars = Array(input)
        var counts = Array(repeating: 0, count: 256)
        var unique = 0

        for (i, c) in chars.enumerated() {
            let c = Int(c.asciiValue!)
            let count = counts[c]
            if count == 0 {
                unique += 1
            }
            counts[c] = count + 1

            if i >= length {
                let c = Int(chars[i - length].asciiValue!)
                let count = counts[c]
                if count == 1 {
                    unique -= 1
                }
                counts[c] = count - 1
            }

            if unique == length {
                return i + 1
            }
        }
        return 0
    }
}
