package poker;

/**
 * Enumeration of poker hand categories ordered from strongest to weakest.
 */
public enum HandRank {
    STRAIGHT_FLUSH("Straight Flush", 9),
    FOUR_OF_A_KIND("Four of a Kind", 8),
    FULL_HOUSE("Full House", 7),
    FLUSH("Flush", 6),
    STRAIGHT("Straight", 5),
    THREE_OF_A_KIND("Three of a Kind", 4),
    TWO_PAIR("Two Pair", 3),
    ONE_PAIR("One Pair", 2),
    HIGH_CARD("High Card", 1);

    private final String displayName;
    private final int strength;

    HandRank(String displayName, int strength) {
        this.displayName = displayName;
        this.strength = strength;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getStrength() {
        return strength;
    }
}
