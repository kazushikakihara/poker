package poker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entry point for the console poker hand evaluation application.
 */
public final class PokerHandApp {

    private PokerHandApp() {
        // Utility class
    }

    public static void main(String[] args) {
        PokerHandEvaluator evaluator = new PokerHandEvaluator();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line = reader.readLine();
            List<Card> cards = parseInput(line);
            HandResult result = evaluator.evaluate(cards);
            System.out.println(result.getHandRank().getDisplayName());
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Invalid input");
            System.exit(1);
        }
    }

    private static List<Card> parseInput(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Input must contain five cards");
        }
        String[] tokens = line.trim().split("\\s+");
        if (tokens.length != 5) {
            throw new IllegalArgumentException("Exactly five cards are required");
        }
        List<Card> cards = new ArrayList<>(5);
        Set<Card> uniqueCards = new HashSet<>();
        for (String token : tokens) {
            Card card = Card.fromString(token);
            if (!uniqueCards.add(card)) {
                throw new IllegalArgumentException("Duplicate card detected");
            }
            cards.add(card);
        }
        return cards;
    }
}
