package poker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Enumeration of the card ranks with numeric strength values.
 */
public enum Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10, "T"),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    ACE("A", 14);

    private static final Map<String, Rank> LOOKUP = buildLookup();

    private final String primarySymbol;
    private final int value;
    private final String[] aliases;

    Rank(String primarySymbol, int value, String... aliases) {
        this.primarySymbol = primarySymbol;
        this.value = value;
        this.aliases = aliases;
    }

    public int getValue() {
        return value;
    }

    public String getPrimarySymbol() {
        return primarySymbol;
    }

    private static Map<String, Rank> buildLookup() {
        Map<String, Rank> map = new HashMap<>();
        for (Rank rank : values()) {
            map.put(rank.primarySymbol, rank);
            for (String alias : rank.aliases) {
                map.put(alias, rank);
            }
        }
        return map;
    }

    /**
     * Resolves the text representation of a rank into a {@link Rank} value.
     *
     * @param text rank symbol (case insensitive)
     * @return rank
     */
    public static Rank fromSymbol(String text) {
        String normalized = text.toUpperCase(Locale.ROOT);
        Rank rank = LOOKUP.get(normalized);
        if (rank == null) {
            throw new IllegalArgumentException("Unknown rank: " + text);
        }
        return rank;
    }
}
