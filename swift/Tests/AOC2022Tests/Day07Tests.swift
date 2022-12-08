@testable import AOC2022
import XCTest

final class Day07Tests: XCTestCase {
    func testExample() throws {
        let example = """
        $ cd /
        $ ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        $ cd a
        $ ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        $ cd e
        $ ls
        584 i
        $ cd ..
        $ cd ..
        $ cd d
        $ ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
        """
        XCTAssertEqual(try Day07.solve1(example), 95437)
        XCTAssertEqual(try Day07.solve2(example), 24933642)
    }

    func testInput() throws {
        let input = try Input.read("day07")
        XCTAssertEqual(try Day07.solve1(input), 1743217)
        XCTAssertEqual(try Day07.solve2(input), 8319096)
    }
}
