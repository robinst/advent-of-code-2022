import Foundation

enum InputError: Error {
    case resourceNotFound(String)
    case parseError(String)
}

enum Input {
    static func read(_ resource: String) throws -> String {
        guard let url = Bundle.module.url(forResource: resource, withExtension: "txt", subdirectory: "Resources") else {
            throw InputError.resourceNotFound(resource)
        }

        let data = try Data(contentsOf: url)
        return String(decoding: data, as: UTF8.self).trimmingCharacters(in: ["\n"])
    }
}
