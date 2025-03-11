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

    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.remainingGames = games.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

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


    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

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

    @Override
    public void reset() {
        this.remainingGames = games.stream();
    }
}
