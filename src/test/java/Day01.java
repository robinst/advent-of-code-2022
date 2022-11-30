import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01 {

    public static long solve(String input) {
        return input.lines().count();
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day01.txt"));
        System.out.println(solve(input));
    }

    @Test
    void foo() {
        assertEquals(2, solve("""
                test
                foo
                """));
    }
}
