package student;

import java.util.Comparator;
import java.util.stream.Stream;

public class Sorting {

    private Sorting() {}

    public static Stream<BoardGame> sort(Stream<BoardGame> games, GameData sortOn, boolean ascending) {
        // Build a comparator based on the `sortOn` logic
        Comparator<BoardGame> comparator =
                Comparator.comparing((BoardGame g) -> g.toStringWithInfo(sortOn).toLowerCase());

        // If not ascending, reverse the comparator
        if (!ascending) {
            comparator = comparator.reversed();
        }

        return games.sorted(comparator);
    }
}
