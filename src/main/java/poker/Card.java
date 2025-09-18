package poker;

import java.util.Locale;
import java.util.Objects;

/**
 * Immutable representation of a playing card.
 */
public final class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = Objects.requireNonNull(rank, "rank");
        this.suit = Objects.requireNonNull(suit, "suit");
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRankValue() {
        return rank.getValue();
    }

    /**
     * Parses a card token such as {@code AS} or {@code 10H}.
     *
     * @param token string representation
     * @return card instance
     */
    public static Card fromString(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }
        String trimmed = token.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be empty");
        }
        String normalized = trimmed.toUpperCase(Locale.ROOT);
        if (normalized.length() < 2) {
            throw new IllegalArgumentException("Token is too short: " + token);
        }
        char suitChar = normalized.charAt(normalized.length() - 1);
        Suit suit = Suit.fromSymbol(suitChar);
        String rankPart = normalized.substring(0, normalized.length() - 1);
        Rank rank = Rank.fromSymbol(rankPart);
        return new Card(rank, suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card card)) {
            return false;
        }
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        return rank.getPrimarySymbol() + suit.getSymbol();
    }
}
