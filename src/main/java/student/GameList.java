package student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A concrete implementation of IGameList.
 * this class uses a HashSet to store BoardGame objects,based on the equal logic.
 * Games are returned in case-insensitive ascending order based on their name.
 * The class also provides functionality to add and remove games by name
 * or by integer index ranges, as well as saving the list of games to a file.
 */
public class GameList implements IGameList {

    /**
     * Internal storage for the board games, ensuring no duplicates
     * based on equals Object.
     */
    private final Set<BoardGame> games;

    /**
     * Constructor for the GameList.
     */
    public GameList() {
        this.games = new HashSet<>();
    }

    /**
     * @return a sorted list (ascending, ignoring case) of all game names
     */
    @Override
    public List<String> getGameNames() {
        return games.stream().map(BoardGame::getName).
                sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
    }

    /**
     * <p>Removes all games from this list.</p>
     */
    @Override
    public void clear() {
        games.clear();
    }

    /**
     * Returns the number of games stored in this list.
     *
     * @return the count of games
     */
    @Override
    public int count() {
        return games.size();
    }

    /**
     * Saves the list of game names to a file.

     * Each game name is written on a new line in the same order as returned by getGameNames().
     * If the file's parent directories do not exist, they are created. If the file already exists, it
     * is overwritten.
     * @param filename the path to the file where game names will be saved
     * @throws RuntimeException if an I/O error occurs during file writing
     */
    @Override
    public void saveGame(String filename) {
        List<String> sortedNames = getGameNames();
        File file = new File(filename);
        // Ensure parent directories exist
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String name : sortedNames) {
                writer.write(name);
                writer.newLine();
            }
        } catch (IOException e) {
            // Depending on requirements, handle or rethrow.
            throw new RuntimeException("Error writing to file: " + e.getMessage(), e);
        }
    }


    /**
     * Adds game(s) to this list based on the provided string.
     * The string may represent a single game name, a numeric index or index range, or the special
     * keyword IGameList When a number or range is specified, the game(s) are chosen
     * from the filtered stream provided as a basis.
     * @param str      the string specifying which game(s) to add
     * @param filtered a stream of BoardGame objects to use as the source for the addition
     * @throws IllegalArgumentException if the string is not valid or no game matches the criteria
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        // ADD_ALL special case
        if (str.equalsIgnoreCase(IGameList.ADD_ALL)) {
            games.addAll(filtered.toList());
        } else if (str.matches("\\d+(-\\d+)?")) {
            games.addAll(filterByIndex(str, filtered));
        } else {
            BoardGame found = findByName(str, filtered);
            if (found == null) {
                throw new IllegalArgumentException(
                        "No game named \"" + str + "\" found in filtered list");
            }
            games.add(found);
        }
    }

    /**
     * Removes game(s) from this list based on the provided string.
     * The string may specify a single game name, a numeric index or index range, or the special
     * keyword  IGameList which causes the entire list to be cleared.
     * @param str the string specifying which game(s) to remove
     * @throws IllegalArgumentException if the string is not valid or no matching games are found
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        List<BoardGame> toRemove = new ArrayList<>();
        // ADD_ALL special case
        if (str.equalsIgnoreCase(IGameList.ADD_ALL)) {
            clear();
        } else if (str.matches("\\d+(-\\d+)?")) {
            toRemove = filterByIndexList(str, new ArrayList<>(games));
        } else {
            BoardGame found = findByName(str, games.stream());
            if (found != null) {
                toRemove.add(found);
            }
        }

        toRemove.forEach(games::remove);
    }


    /**
     * Helper method that filters a list of board games by a numeric index or range.
     * Uses 1-based indexing. If the string contains a dash, it is interpreted as a range (e.g., "1-3").
     * Otherwise, it is treated as a single index.
     * @param str          the index or range string (e.g., "1" or "2-4")
     * @param filteredList the list of BoardGame objects to filter
     * @return a list containing the board game(s) corresponding to the specified index or range
     * @throws IllegalArgumentException if the index or range is out of bounds or invalid
     */
    private List<BoardGame> filterByIndexList(String str, List<BoardGame> filteredList)
            throws IllegalArgumentException {
        // Index range
        if (str.contains("-")) {
            String[] parts = str.split("-");
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            if (start <= 0 || end <= 0 || start > end || end > filteredList.size()) {
                throw new IllegalArgumentException("Index out of range: " + str);
            }
            return new ArrayList<>(filteredList.subList(start - 1, end));
        }

        // Single integer
        int index = Integer.parseInt(str);
        if (index <= 0 || index > filteredList.size()) {
            throw new IllegalArgumentException("Index out of range: " + index);
        }

        return List.of(filteredList.get(index - 1));
    }

    /**
     * Helper method that converts a filtered stream to a list and applies numeric index filtering.
     * @param str      the index or range string
     * @param filtered a stream of BoardGame objects
     * @return a list of board games that correspond to the index or range
     * @throws IllegalArgumentException if the index or range is invalid
     */
    private List<BoardGame> filterByIndex(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredList = filtered.toList();
        return filterByIndexList(str, filteredList);
    }

    /**
     * Helper method that finds a board game by name within a given stream.
     * The comparison is case-insensitive.
     * @param str    the game name to search for
     * @param stream a stream of BoardGame objects
     * @return the first matching BoardGame if found; null otherwise
     * @throws IllegalArgumentException if an error occurs during the search
     */
    private BoardGame findByName(String str, Stream<BoardGame> stream) throws IllegalArgumentException {
        return stream
                .filter(bg -> bg.getName().equalsIgnoreCase(str))
                .findFirst()
                .orElse(null);
    }

}
