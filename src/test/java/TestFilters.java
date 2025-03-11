import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import student.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFilters {
    private BoardGame sampleGame;

    @BeforeEach
    public void setUp() {
        sampleGame = new BoardGame("Pandemic", 1, 2, 4, 30, 60, 4.0, 5, 8.5, 2008);
    }


    @Test
    public void testFilterNumberFloatLessThan() {
        boolean result = Filters.filter(sampleGame, GameData.RATING, Operations.LESS_THAN, "9.0");
        assertTrue(result);
    }

    @Test
    public void testFilterNumEquals() {
        boolean result = Filters.filter(sampleGame, GameData.MAX_PLAYERS, Operations.EQUALS, "4");
        assertTrue(result);
    }

    @Test
    public void testFilterNumNotEquals() {
        boolean result = Filters.filter(sampleGame, GameData.MIN_PLAYERS, Operations.NOT_EQUALS, "3");
        assertTrue(result);
    }

    @Test
    public void testFilterBoardGameName() {
        boolean result = Filters.filter(sampleGame, GameData.NAME, Operations.EQUALS, "pandemic");
        assertTrue(result);

        result = Filters.filter(sampleGame, GameData.NAME, Operations.NOT_EQUALS, "Outbreak");
        assertTrue(result);
    }

    @Test
    public void testFilterBoardGameNumericField() {
        boolean result = Filters.filter(sampleGame, GameData.MAX_PLAYERS, Operations.EQUALS, "4");
        assertTrue(result);

        result = Filters.filter(sampleGame, GameData.YEAR, Operations.GREATER_THAN, "2000");
        assertTrue(result);
    }
}
