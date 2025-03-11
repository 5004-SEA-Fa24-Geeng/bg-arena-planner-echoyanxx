package student;

import java.util.Comparator;
import java.util.stream.Stream;
/**
 * A utility class that provides sorting functionality for BoardGame objects
 * based on a specified attribute.
 */

public final class Sorting {

    private Sorting() { }

    /**
     * Sorts a stream of BoardGame objects by the specified game data attribute.
     * The sorting is performed by comparing the string representation of each
     *
     * @param games     the stream of BoardGame objects to be sorted
     * @param sortOn    the attribute column to sort by
     * @param ascending if code true, sort in ascending order; otherwise, sort in descending order
     * @return a new Stream<BoardGame> that is sorted according to the specified parameters
     */
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
