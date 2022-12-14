@testable import AOC2022
import XCTest

final class Day09Tests: XCTestCase {
    func testExample() throws {
        let example = """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
        """
        XCTAssertEqual(Day09.solve1(example), 13)

        let example2 = """
        R 5
        U 8
        L 8
        D 3
        R 17
        D 10
        L 25
        U 20
        """
        XCTAssertEqual(Day09.solve2(example2), 36)
    }

    func testInput() throws {
        let input = try Input.read("day09")
        XCTAssertEqual(Day09.solve1(input), 6271)
        XCTAssertEqual(Day09.solve2(input), 2458)
    }
}
