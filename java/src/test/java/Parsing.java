import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Parsing {
    
    static List<Integer> numbers(String input) {
        var digits = input.split("[^\\d]+");
        return Arrays.stream(digits)
                .filter(p -> !p.isEmpty())
                .flatMap(s -> {
                    try {
                        return Stream.of(Integer.parseInt(s));
                    } catch (NumberFormatException e) {
                        return Stream.empty();
                    }
                }).toList();
    }
}