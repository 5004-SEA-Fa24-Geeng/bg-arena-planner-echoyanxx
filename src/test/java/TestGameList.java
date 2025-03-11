import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import student.GameList;
import student.IGameList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class TestGameList {
    private IGameList gameList;
    private Set<BoardGame> sampleGames;

    @BeforeEach
    public void setUp() {
        sampleGames = new HashSet<>();
        sampleGames.add(new BoardGame("Catan", 1, 3, 4, 60, 90, 3.5, 10, 7.5, 1995));
        sampleGames.add(new BoardGame("Chess", 2, 2, 2, 5, 30, 2.0, 20, 8.0, 2000));
        sampleGames.add(new BoardGame("Monopoly", 3, 4, 6, 30, 120, 1.5, 30, 6.0, 1990));
        sampleGames.add(new BoardGame("Go", 4, 2, 5, 30, 60, 4.0, 15, 9.0, 2005));

        // Create a new GameList instance and add all sample games.
        gameList = new GameList();
        gameList.addToList(IGameList.ADD_ALL, sampleGames.stream());
    }

    @Test
    public void testGetGameNames() {
        List<String> names = gameList.getGameNames();
        assertEquals(4, names.size());
        assertEquals("Catan", names.get(0));
        assertEquals("Chess", names.get(1));
        assertEquals("Go", names.get(2));
        assertEquals("Monopoly", names.get(3));
    }

    @Test
    public void testCountAndClear() {
        assertEquals(4, gameList.count());
        gameList.clear();
        assertEquals(0, gameList.count());
    }

    @Test
    public void testAddToListByIndex() {
        IGameList newList = new GameList();
        // Create a sorted stream of sampleGames based on game name (case-insensitive)
        Stream<BoardGame> sortedStream = sampleGames.stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        // Add the first game from the sorted stream by specifying index "1"
        newList.addToList("1", sortedStream);
        assertEquals(1, newList.count());
    }

    @Test
    public void testAddToListByName() {
        IGameList newList = new GameList();
        newList.addToList("Chess", sampleGames.stream());
        assertEquals(1, newList.count());
        List<String> names = newList.getGameNames();
        assertEquals("Chess", names.get(0));
    }

    @Test
    public void testRemoveFromListByIndex() {
        IGameList newList = new GameList();
        newList.addToList(IGameList.ADD_ALL, sampleGames.stream());
        int initialCount = newList.count();
        // Remove the first game (numeric index "1")
        newList.removeFromList("1");
        assertEquals(initialCount - 1, newList.count());
    }

    @Test
    public void testRemoveFromListByName() {
        IGameList newList = new GameList();
        newList.addToList(IGameList.ADD_ALL, sampleGames.stream());
        int initialCount = newList.count();
        // Remove "Monopoly" by name.
        newList.removeFromList("Monopoly");
        assertEquals(initialCount - 1, newList.count());
        List<String> names = newList.getGameNames();
        assertTrue(names.stream().noneMatch(name -> name.equalsIgnoreCase("Monopoly")));
    }

    @Test
    public void testAddToListInvalidName() {
        IGameList newList = new GameList();
        assertThrows(IllegalArgumentException.class, () -> {
            newList.addToList("NonExistentGame", sampleGames.stream());
        });
    }

    @Test
    public void testSaveGame() throws IOException {
        IGameList newList = new GameList();
        newList.addToList(IGameList.ADD_ALL, sampleGames.stream());
        // Create a temporary file.
        File tempFile = File.createTempFile("gamelist", ".txt");
        tempFile.deleteOnExit();
        newList.saveGame(tempFile.getAbsolutePath());

        List<String> fileContents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.add(line);
            }
        }
        List<String> expected = newList.getGameNames();
        assertEquals(expected, fileContents, "The file should contain the sorted game names, one per line.");
    }
}
