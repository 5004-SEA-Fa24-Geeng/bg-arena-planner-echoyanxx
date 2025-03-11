package student;


import java.util.Set;
import java.util.stream.Stream;


/**
 * A planner that applies various filters and sorting options to a collection of BoardGame objects.
 */

public class Planner implements IPlanner {

    /**
     * A {Set of BoardGame objects.
     */
    private final Set<BoardGame> games;

    /**
     * A stream representing the remaining (filtered) games after applying filter operations.
     */
    private Stream<BoardGame> remainingGames;

    /**
     * Constructs a Planner with the given set of board games.
     * Initializes the internal stream to contain all games.
     *
     * @param games the collection of  BoardGame objects to manage and filter
     */
    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.remainingGames = games.stream();
    }

    /**
     * Applies the specified filter to the board games and returns a stream of games that match the filter.
     * This is a convenience method that sorts the result by GameData#NAME in ascending order.
     *
     * @param filter the filter to apply, specified as a comma-separated list of filter expressions
     * @return a stream of BoardGame}objects that match the filter
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    /**
     * Applies a single filter expression on a given stream of board games.
     * The expression is parsed to extract the operation and the value, and then applied to each game using
     * the Filters(BoardGame, GameData, Operations, String)} method.
     *
     * @param filter        a single filter expression (e.g. "minPlayers>4")
     * @param filteredGames the stream of games to filter
     * @return a new stream containing only the games that satisfy the filter condition; if the filter is invalid,
     *         the original stream is returned unmodified.
     */
    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        //handle getting operation, game attribute to filter on
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }
        //remove space
        filter = filter.replaceAll(" ", "");

        String[] parts = filter.split(operator.getOperator());
        if (parts.length != 2) {
            return filteredGames;
        }
        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }
        //more work here to filter the games
        //we found creating a String filter and a Number filter to be useful.
        //both of them take in both the GameDate enum, Operator Enum, and the value to parse and filter
        String value;
        try {
            value = parts[1].trim();
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }
        System.out.print("Operator is :" + operator);
        System.out.print("GameData is :" + column);
        System.out.println(" Value is :" + value);

        return filteredGames.filter(game -> Filters.filter(game, column, operator, value));
    }

    /**
     * Applies the specified filter to the board games, then sorts the results by the given column in ascending order.
     *
     * @param filter the filter to apply, specified as a comma-separated list of filter expressions
     * @param sortOn the GameData}attribute to sort on
     * @return a stream of BoardGame objects that match the filter, sorted by {@code sortOn} in ascending order
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }


    /**
     * Applies the specified filter to the board games, then sorts the results by the given column
     * in the specified order.
     * @param filter    the filter to apply, specified as a comma-separated list of filter expressions
     * @param sortOn    the GameData attribute to sort on
     * @param ascending if @code true, sort in ascending order; otherwise, sort in descending order
     * @return a stream of  BoardGame objects that match the filter, sorted according to the specified parameters
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // Split the filters
        String[] filterList = filter.split(",");

        // Apply each filter in order
        for (String oneFilter : filterList) {
            remainingGames = filterSingle(oneFilter, remainingGames);
        }

        return Sorting.sort(remainingGames, sortOn, ascending);
    }

    /**
     * Resets the filtering process, restoring the internal stream to contain all board games.
     * After a reset, any filters applied previously are discarded.
     */
    @Override
    public void reset() {
        this.remainingGames = games.stream();
    }
}
