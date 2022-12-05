enum Day05 {
    struct Move {
        var count: Int
        var from: Int
        var to: Int
    }

    static func solve1(_ input: String) throws -> String {
        var (stacks, instructions) = parse(input)

        for instruction in instructions {
            let from = instruction.from - 1
            let to = instruction.to - 1
            for _ in 0 ..< instruction.count {
                if let item = stacks[from].popLast() {
                    stacks[to].append(item)
                }
            }
        }
        return stacks.map { String($0.last!) }.joined()
    }

    static func solve2(_ input: String) throws -> String {
        var (stacks, instructions) = parse(input)

        for instruction in instructions {
            let from = instruction.from - 1
            let to = instruction.to - 1
            stacks[to].append(contentsOf: stacks[from].suffix(instruction.count))
            stacks[from].removeLast(instruction.count)
        }
        return stacks.map { String($0.last!) }.joined()
    }

    static func parse(_ input: String) -> ([[Character]], [Move]) {
        let parts = input.components(separatedBy: "\n\n")

        var stacks: [[Character]] = []
        let lines = parts[0].components(separatedBy: "\n").dropLast(1)
        for line in lines {
            let chars = Array(line)
            for i in stride(from: 1, to: chars.count, by: 4) {
                let c = chars[i]
                let stack = i / 4
                if stacks.count <= stack {
                    stacks.append([])
                }
                if c != " " {
                    stacks[stack].insert(c, at: 0)
                }
            }
        }

        let instructions = parts[1].components(separatedBy: "\n")
            .map { line in
                let nums = numbers(line)
                return Move(count: nums[0], from: nums[1], to: nums[2])
            }
        return (stacks, instructions)
    }
}
