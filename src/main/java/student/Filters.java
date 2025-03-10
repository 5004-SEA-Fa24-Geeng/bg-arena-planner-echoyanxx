package student;

/**
 *
 */
public final class Filters {
    private Filters() {
    }

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
                    throw new IllegalArgumentException("The column " + column.getColumnName() + " is not supported in filtering");
        };
    }

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
