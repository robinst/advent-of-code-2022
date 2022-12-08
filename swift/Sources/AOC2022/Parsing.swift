func numbers(_ input: String) -> [Int] {
    input.components(separatedBy: .whitespaces)
        .compactMap { Int($0) }
}

extension String {
    func deletingPrefix(_ prefix: String) -> String {
        guard self.hasPrefix(prefix) else { return self }
        return String(self.dropFirst(prefix.count))
    }
}
