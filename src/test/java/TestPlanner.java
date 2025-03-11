import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import student.Planner;
import student.IPlanner;
import student.GameData;


/**
 * JUnit test for the Planner class.
 *
 * Just a sample test to get you started, also using
 * setup to help out. 
 */
public class TestPlanner {
    static Set<BoardGame> games;

    @BeforeAll
    public static void setup() {
        games = new HashSet<>();
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));
    }

     @Test
    public void testFilterName() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name == Go").toList();
        assertEquals(1, filtered.size());
        assertEquals("Go", filtered.get(0).getName());
    }

    @Test
    public void testFilterNameContains() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name~=go").toList();
        assertEquals(4, filtered.size());

        boolean hasGo = filtered.stream().anyMatch(g -> g.getName().equalsIgnoreCase("Go"));
        boolean hasGolang = filtered.stream().anyMatch(g -> g.getName().equalsIgnoreCase("golang"));
        assertTrue(hasGo);
        assertTrue(hasGolang);
    }

    @Test
    public void testMultipleFilters() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers>1,maxPlayers<6").toList();
        assertEquals(2, filtered.size());
        assertEquals("Chess", filtered.get(0).getName());
        assertEquals("Go", filtered.get(1).getName());
    }

    @Test
    public void testSortingDescending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> sortedDesc = planner.filter("", GameData.NAME, false).toList();
        String first = sortedDesc.get(0).getName();
        String second = sortedDesc.get(1).getName();
        assertTrue(first.compareToIgnoreCase(second) >= 0);
    }

    @Test
    public void testReset() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name == Go").toList();
        assertEquals(1, filtered.size());

        planner.reset();
        List<BoardGame> fullList = planner.filter("", GameData.NAME, true).toList();
        assertEquals(games.size(), fullList.size());
    }
}