package poker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Core hand evaluation logic. It derives the strongest hand category and captures tie breakers for future comparison.
 */
public class PokerHandEvaluator {

    /**
     * Evaluates exactly five cards.
     *
     * @param cards hand of cards
     * @return evaluation result containing the category and tie breakers
     */
    public HandResult evaluate(List<Card> cards) {
        Objects.requireNonNull(cards, "cards");
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Exactly five cards are required");
        }

        Map<Integer, Long> rankCounts = buildRankHistogram(cards);
        boolean isFlush = isFlush(cards);
        int straightHigh = detectStraightHigh(cards);
        boolean isStraight = straightHigh != -1;

        // Sort ranks grouped by their occurrence count (e.g. 3-of-a-kind, pairs) for easier pattern detection.
        List<Map.Entry<Integer, Long>> groupedByCount = new ArrayList<>(rankCounts.entrySet());
        groupedByCount.sort((a, b) -> {
            int countCompare = Long.compare(b.getValue(), a.getValue());
            if (countCompare != 0) {
                return countCompare;
            }
            return Integer.compare(b.getKey(), a.getKey());
        });

        List<Integer> sortedRanksDesc = cards.stream()
                .map(Card::getRankValue)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (isStraight && isFlush) {
            return new HandResult(HandRank.STRAIGHT_FLUSH, List.of(straightHigh));
        }

        if (groupedByCount.get(0).getValue() == 4) {
            int fourKindRank = groupedByCount.get(0).getKey();
            int kicker = groupedByCount.get(1).getKey();
            return new HandResult(HandRank.FOUR_OF_A_KIND, List.of(fourKindRank, kicker));
        }

        if (groupedByCount.get(0).getValue() == 3 && groupedByCount.get(1).getValue() == 2) {
            int threeRank = groupedByCount.get(0).getKey();
            int pairRank = groupedByCount.get(1).getKey();
            return new HandResult(HandRank.FULL_HOUSE, List.of(threeRank, pairRank));
        }

        if (isFlush) {
            return new HandResult(HandRank.FLUSH, sortedRanksDesc);
        }

        if (isStraight) {
            return new HandResult(HandRank.STRAIGHT, List.of(straightHigh));
        }

        if (groupedByCount.get(0).getValue() == 3) {
            int tripleRank = groupedByCount.get(0).getKey();
            List<Integer> kickers = groupedByCount.stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toCollection(ArrayList::new));
            List<Integer> tiebreakers = new ArrayList<>();
            tiebreakers.add(tripleRank);
            tiebreakers.addAll(kickers);
            return new HandResult(HandRank.THREE_OF_A_KIND, tiebreakers);
        }

        if (groupedByCount.get(0).getValue() == 2 && groupedByCount.get(1).getValue() == 2) {
            List<Integer> pairRanks = groupedByCount.stream()
                    .filter(entry -> entry.getValue() == 2)
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            int kicker = groupedByCount.stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
            List<Integer> tiebreakers = new ArrayList<>(pairRanks);
            tiebreakers.add(kicker);
            return new HandResult(HandRank.TWO_PAIR, tiebreakers);
        }

        if (groupedByCount.get(0).getValue() == 2) {
            int pairRank = groupedByCount.get(0).getKey();
            List<Integer> kickers = groupedByCount.stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toCollection(ArrayList::new));
            List<Integer> tiebreakers = new ArrayList<>();
            tiebreakers.add(pairRank);
            tiebreakers.addAll(kickers);
            return new HandResult(HandRank.ONE_PAIR, tiebreakers);
        }

        return new HandResult(HandRank.HIGH_CARD, sortedRanksDesc);
    }

    private Map<Integer, Long> buildRankHistogram(List<Card> cards) {
        Map<Integer, Long> counts = new HashMap<>();
        for (Card card : cards) {
            counts.merge(card.getRankValue(), 1L, Long::sum);
        }
        return counts;
    }

    private boolean isFlush(List<Card> cards) {
        Suit firstSuit = cards.get(0).getSuit();
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getSuit() != firstSuit) {
                return false;
            }
        }
        return true;
    }

    private int detectStraightHigh(List<Card> cards) {
        List<Integer> sorted = cards.stream()
                .map(Card::getRankValue)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i).equals(sorted.get(i - 1))) {
                return -1; // Duplicate ranks cannot form a straight.
            }
        }

        // Handle wheel straight A-2-3-4-5 explicitly by converting Ace to low.
        if (sorted.get(0) == 2 && sorted.get(1) == 3 && sorted.get(2) == 4 && sorted.get(3) == 5 && sorted.get(4) == 14) {
            return 5;
        }

        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) - sorted.get(i - 1) != 1) {
                return -1;
            }
        }
        return sorted.get(sorted.size() - 1);
    }
}
