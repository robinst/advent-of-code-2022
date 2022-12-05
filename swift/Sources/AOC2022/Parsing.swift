func numbers(_ input: String) -> [Int] {
    input.components(separatedBy: .whitespaces)
        .compactMap { Int($0) }
}
