@testable import AOC2022
import XCTest

final class ParsingTests: XCTestCase {
    func testNumbers() throws {
        XCTAssertEqual(numbers("move 1 from 2 to 1"), [1, 2, 1])
    }
}
