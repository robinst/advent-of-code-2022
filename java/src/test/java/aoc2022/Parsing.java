package aoc2022;

import java.util.ArrayList;
import java.util.List;

public class Parsing {

    static List<Integer> numbers(String input) {
        var digits = input.split("[^\\d-]+");
        var result = new ArrayList<Integer>();
        for (String s : digits) {
            if (s.isEmpty()) {
                continue;
            }
            try {
                result.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return result;
    }

    static List<Long> numbersLong(String input) {
        var digits = input.split("[^\\d-]+");
        var result = new ArrayList<Long>();
        for (String s : digits) {
            if (s.isEmpty()) {
                continue;
            }
            try {
                result.add(Long.parseLong(s));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return result;
    }
}