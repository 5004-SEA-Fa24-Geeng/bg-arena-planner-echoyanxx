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

    @Override
    public int count() {
        return games.size();
    }

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

    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        // ADD_ALL special case
        if (str.equalsIgnoreCase(IGameList.ADD_ALL)) {
            games.addAll(filtered.toList());}
        // integer(s)
        else if (str.matches("\\d+(-\\d+)?")) {
            games.addAll(filterByIndex(str, filtered));}
        // Game name
        else {
            BoardGame found = findByName(str, filtered);
            if (found == null) {
                throw new IllegalArgumentException(
                        "No game named \"" + str + "\" found in filtered list");
            }
            games.add(found);
        }
    }

    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        List<BoardGame> toRemove = new ArrayList<>();
        // ADD_ALL special case
        if (str.equalsIgnoreCase(IGameList.ADD_ALL)) {
            clear();}
        // integer(s)
        else if (str.matches("\\d+(-\\d+)?")) {
            toRemove = filterByIndexList(str, new ArrayList<>(games));}
        // Game name
        else {
            BoardGame found = findByName(str, games.stream());
            if (found != null) {
                toRemove.add(found);
            }
        }

        toRemove.forEach(games::remove);
    }

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


    private List<BoardGame> filterByIndex(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredList = filtered.toList();
        return filterByIndexList(str, filteredList);
    }

    private BoardGame findByName(String str, Stream<BoardGame> stream) throws IllegalArgumentException {
        return stream
                .filter(bg -> bg.getName().equalsIgnoreCase(str))
                .findFirst()
                .orElse(null);
    }

}
