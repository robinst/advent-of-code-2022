@testable import AOC2022
import XCTest

final class Day01Tests: XCTestCase {
    func testExample() throws {
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
        XCTAssertEqual(Day01.solve1(example), 24000)
        XCTAssertEqual(Day01.solve2(example), 45000)
    }

    func testInput() throws {
        let input = try Input.read("day01")
        XCTAssertEqual(Day01.solve1(input), 69795)
        XCTAssertEqual(Day01.solve2(input), 208437)
    }
}
