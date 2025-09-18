package poker;

import java.util.Map;

/**
 * Enumeration of the four suits in a standard deck of playing cards.
 */
public enum Suit {
    SPADES('S'),
    HEARTS('H'),
    DIAMONDS('D'),
    CLUBS('C');

    private static final Map<Character, Suit> LOOKUP = Map.of(
            'S', SPADES,
            'H', HEARTS,
            'D', DIAMONDS,
            'C', CLUBS
    );

    private final char symbol;

    Suit(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * Parses a single character into a {@link Suit} value.
     *
     * @param symbol suit symbol
     * @return matching suit
     * @throws IllegalArgumentException if the symbol is unknown
     */
    public static Suit fromSymbol(char symbol) {
        Suit suit = LOOKUP.get(Character.toUpperCase(symbol));
        if (suit == null) {
            throw new IllegalArgumentException("Unknown suit: " + symbol);
        }
        return suit;
    }
}
