package student;

/**
 * A class that provides filtering logic for BoardGame objects
 * based on various attributes (such as name, players, rating, etc.) and
 * comparison operations.
 */
public final class Filters {
    private Filters() {
    }

/**
 * Applies a filter on a single BoardGame object using the
 * specified column, operation, and value.
 * Based on the column, this method delegates to one of the
 * appropriate helper methods
 * @param game   the BoardGame instance to check
 * @param column the specific attribute/column of the BoardGame to be filtered
 * @param op     the comparison operation (e.g., EQUALS, GREATER_THAN)
 * @param value  the string value to compare against
 * @return true if the game matches the filter conditions, return false otherwise
 * @throws IllegalArgumentException if the requested column is unsupported
 */
public static boolean filter(BoardGame game, GameData column,
                                 Operations op, String value) {
        return switch (column) {
            case NAME ->
                //filter the name
                    filterString(game.getName().toLowerCase(), op, value.toLowerCase());
            case MAX_PLAYERS ->
                //filter based on max-players
                    filterNum(game.getMaxPlayers(), op, value);
            case MIN_PLAYERS -> filterNum(game.getMinPlayers(), op, value);
            case RANK -> filterNum(game.getRank(), op, value);
            case YEAR -> filterNum(game.getYearPublished(), op, value);
            case MAX_TIME -> filterNum(game.getMaxPlayTime(), op, value);
            case MIN_TIME -> filterNum(game.getMinPlayTime(), op, value);
            case RATING -> filterNumberFloat(game.getRating(), op, value);
            case DIFFICULTY -> filterNumberFloat(game.getDifficulty(), op, value);
            default ->
                    throw new IllegalArgumentException("The column " +
                            column.getColumnName() + " is not supported in filtering");
        };
    }

    /**
     * Filters a string value based on a given operation. For example, if op is EQUALS,
     * this method checks whether gameData is exactly equal to value, ignoring
     * case if that is desired by the caller.
     *
     * @param gameData the actual string to test
     * @param op       the comparison operation
     * @param value    the string value to compare with gameData
     * @return {@code true} if {@code gameData} meets the condition defined by op
     */
    public static boolean filterString(String gameData, Operations op, String value) {
        gameData = gameData.replaceAll(" ", "");
        return switch (op) {
            case EQUALS -> gameData.equals(value);
            case NOT_EQUALS -> !gameData.equalsIgnoreCase(value);
            case CONTAINS -> gameData.contains(value);
            case LESS_THAN_EQUALS -> gameData.compareTo(value) <= 0;
            case GREATER_THAN_EQUALS -> gameData.compareTo(value) >= 0;
            case LESS_THAN -> gameData.compareTo(value) < 0;
            case GREATER_THAN -> gameData.compareTo(value) > 0;
        };
    }

    public static boolean filterNumberFloat(double gameData, Operations op, String value) {
        double floatValue = Double.parseDouble(value);

        return switch (op) {
            case EQUALS -> gameData == floatValue;
            case GREATER_THAN -> gameData > floatValue;
            case LESS_THAN -> gameData < floatValue;
            case GREATER_THAN_EQUALS -> gameData >= floatValue;
            case NOT_EQUALS -> gameData != floatValue;
            case LESS_THAN_EQUALS -> gameData <= floatValue;
            default -> throw new IllegalArgumentException("Invalid operation: " + op);
        };

    }

    public static boolean filterNum(int gameData, Operations op, String value) {
        return filterNumberFloat(gameData, op, value);
    }
}
