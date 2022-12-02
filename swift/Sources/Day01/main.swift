import Foundation

let input = String(decoding: try Data(contentsOf: Bundle.module.url(forResource: "input", withExtension: "txt")!), as: UTF8.self).trimmingCharacters(in: ["\n"])

let example = """
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
"""

func solve1(_ input: String) -> Int {
    let elves = input.components(separatedBy: "\n\n")
    return elves.map { elf in
        elf.components(separatedBy: "\n")
            .compactMap { Int($0) }
            .reduce(0, +)
    }.max()!
}

func solve2(_ input: String) -> Int {
    let elves = input.components(separatedBy: "\n\n")
    return elves.map { elf in
        elf.components(separatedBy: "\n")
            .compactMap { Int($0) }
            .reduce(0, +)
    }
    .sorted()
    .suffix(3)
    .reduce(0, +)
}

print(solve1(example))
print(solve1(input))

print("-")

print(solve2(example))
print(solve2(input))
