package student;


import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;


public class Planner implements IPlanner {
    Set<BoardGame> games;
    Stream<BoardGame> remainingGames;

    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.remainingGames = games.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    private Stream<BoardGame> filterSingle (String filter, Stream<BoardGame> filteredGames){
        //handle getting operation, game attribute to filter on
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null){
            return filteredGames;
        }
        //remove space
        filter = filter.replaceAll(" ", "");

        String[] parts = filter.split(operator.getOperator());
        if(parts.length != 2){
            return filteredGames;
        }
        GameData column;
        try{
            column = GameData.fromString(parts[0]);
        } catch(IllegalArgumentException e){
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
        // Filters.filter(board game,game data, operator, String value)
        // Stream<BoardGame> filteredGames
        // List<BoardGame> filteredGameList = filteredGames.filter(game -> Filters.filter(game, column, operator, value)).toList();

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

        // Build a comparator based on the `sortOn` logic
        Comparator<BoardGame> comparator =
                Comparator.comparing((BoardGame g) -> g.toStringWithInfo(sortOn));

        // If not ascending, reverse the comparator
        if (!ascending) {
            comparator = comparator.reversed();
        }

        return remainingGames.sorted(comparator);
    }

    @Override
    public void reset() {
        this.remainingGames = games.stream();
    }
}
