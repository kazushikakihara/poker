package poker;

import java.util.List;
import java.util.Objects;

/**
 * Result of evaluating a poker hand containing the category and tie breaker information.
 */
public final class HandResult implements Comparable<HandResult> {
    private final HandRank handRank;
    private final List<Integer> tiebreakers;

    public HandResult(HandRank handRank, List<Integer> tiebreakers) {
        this.handRank = Objects.requireNonNull(handRank, "handRank");
        this.tiebreakers = List.copyOf(Objects.requireNonNull(tiebreakers, "tiebreakers"));
    }

    public HandRank getHandRank() {
        return handRank;
    }

    public List<Integer> getTiebreakers() {
        return tiebreakers;
    }

    @Override
    public int compareTo(HandResult other) {
        Objects.requireNonNull(other, "other");
        int rankComparison = Integer.compare(this.handRank.getStrength(), other.handRank.getStrength());
        if (rankComparison != 0) {
            return rankComparison;
        }
        int length = Math.min(this.tiebreakers.size(), other.tiebreakers.size());
        for (int i = 0; i < length; i++) {
            int comparison = Integer.compare(this.tiebreakers.get(i), other.tiebreakers.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }
        return Integer.compare(this.tiebreakers.size(), other.tiebreakers.size());
    }

    @Override
    public String toString() {
        return "HandResult{" +
                "handRank=" + handRank +
                ", tiebreakers=" + tiebreakers +
                '}';
    }
}
