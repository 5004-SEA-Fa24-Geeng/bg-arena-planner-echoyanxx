import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import student.BoardGame;
import student.Sorting;
import student.GameData;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSorting {


    private List<BoardGame> games;

    @BeforeEach
    public void setUp() {
        games = List.of(
                new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005),
                new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006),
                new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000)
        );
    }

    @Test
    public void testSortAscendingByName() {
        Stream<BoardGame> sorted = Sorting.sort(games.stream(), GameData.NAME, true);
        List<BoardGame> result = sorted.toList();

        assertEquals("17 days", result.get(0).getName());
        assertEquals("Chess", result.get(1).getName());
        assertEquals("Go", result.get(2).getName());
    }


    @Test
    public void testSortDescendingByName() {
        Stream<BoardGame> sorted = Sorting.sort(games.stream(), GameData.NAME, false);
        List<BoardGame> result = sorted.toList();

        assertEquals("Go", result.get(0).getName());
        assertEquals("Chess", result.get(1).getName());
        assertEquals("17 days", result.get(2).getName());
    }
}
