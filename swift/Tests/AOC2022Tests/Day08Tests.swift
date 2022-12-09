@testable import AOC2022
import XCTest

final class Day08Tests: XCTestCase {
    func testExample() throws {
        let example = """
        30373
        25512
        65332
        33549
        35390
        """
        XCTAssertEqual(Day08.solve1(example), 21)
        XCTAssertEqual(Day08.solve2(example), 8)
    }

    func testInput() throws {
        let input = try Input.read("day08")
        XCTAssertEqual(Day08.solve1(input), 1805)
        XCTAssertEqual(Day08.solve2(input), 444528)
    }
}
