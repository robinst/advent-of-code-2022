@testable import AOC2022
import XCTest

final class DayNNTests: XCTestCase {
    func testExample() throws {
        let example = """
        
        """
        XCTAssertEqual(try DayNN.solve1(example), 0)
        XCTAssertEqual(try DayNN.solve2(example), 0)
    }

    func testInput() throws {
        let input = try Input.read("dayNN")
        XCTAssertEqual(try DayNN.solve1(input), 0)
        XCTAssertEqual(try DayNN.solve2(input), 0)
    }
}
