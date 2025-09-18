package poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class PokerHandEvaluatorTest {

    private final PokerHandEvaluator evaluator = new PokerHandEvaluator();

    @Test
    void evaluateStraightFlush() {
        HandResult result = evaluator.evaluate(parseHand("AS KS QS JS 10S"));
        assertEquals(HandRank.STRAIGHT_FLUSH, result.getHandRank());
    }

    @Test
    void evaluateFourOfAKind() {
        HandResult result = evaluator.evaluate(parseHand("9C 9D 9H 9S 2D"));
        assertEquals(HandRank.FOUR_OF_A_KIND, result.getHandRank());
    }

    @Test
    void evaluateFullHouse() {
        HandResult result = evaluator.evaluate(parseHand("3H 3D 3S 8C 8D"));
        assertEquals(HandRank.FULL_HOUSE, result.getHandRank());
    }

    @Test
    void evaluateFlush() {
        HandResult result = evaluator.evaluate(parseHand("2S 4S 6S 8S 10S"));
        assertEquals(HandRank.FLUSH, result.getHandRank());
    }

    @Test
    void invalidCardFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> parseHand("A2S 3S 4S 5S 6S"));
    }

    @Test
    void evaluateStraightWheel() {
        HandResult result = evaluator.evaluate(parseHand("AD 2S 3H 4C 5D"));
        assertEquals(HandRank.STRAIGHT, result.getHandRank());
        assertEquals(List.of(5), result.getTiebreakers());
    }

    @Test
    void evaluateThreeOfAKind() {
        HandResult result = evaluator.evaluate(parseHand("7H 7D 7S QH 2C"));
        assertEquals(HandRank.THREE_OF_A_KIND, result.getHandRank());
    }

    @Test
    void evaluateTwoPair() {
        HandResult result = evaluator.evaluate(parseHand("KH KD 5S 5C 2D"));
        assertEquals(HandRank.TWO_PAIR, result.getHandRank());
    }

    @Test
    void evaluateOnePair() {
        HandResult result = evaluator.evaluate(parseHand("QC QD 5S 7C 9H"));
        assertEquals(HandRank.ONE_PAIR, result.getHandRank());
    }

    @Test
    void evaluateHighCard() {
        HandResult result = evaluator.evaluate(parseHand("AS 7D 5H 3C 2D"));
        assertEquals(HandRank.HIGH_CARD, result.getHandRank());
    }

    private List<Card> parseHand(String input) {
        List<Card> cards = Arrays.stream(input.split("\\s+"))
                .map(Card::fromString)
                .collect(Collectors.toList());
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Exactly five cards expected");
        }
        return cards;
    }
}
