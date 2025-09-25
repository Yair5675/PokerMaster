package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.HoleCards;

import java.util.List;

/**
 * A class whose sole purpose is to find the best poker hand a player can get based on their hole
 * cards and all of the community cards.
 */
public class PokerHandEvaluator {
    public static final int EXPECTED_COMMUNITY_CARDS_COUNT = 5;

    /**
     * Given the {@link HoleCards} of a player and ALL FIVE community cards, the function outputs the best
     * {@link PokerHand} that can be made from 5 of the 7 aforementioned cards.
     * Note that the hole cards may not be used in the final poker hand if the community cards alone
     * offer a better combination, and using the hole cards would worsen it.
     * @param holeCards The two private cards that belong to a player.
     * @param communityCards The 5 public cards which will be combined
     * @return The best poker hand that can be made from the given cards.
     * @throws IllegalArgumentException If the number of community cards given isn't
     *                                  {@link PokerHandEvaluator#EXPECTED_COMMUNITY_CARDS_COUNT}.
     */
    public PokerHand getBestHand(HoleCards holeCards, List<Card> communityCards) {
        if (communityCards.size() != EXPECTED_COMMUNITY_CARDS_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Expected %d community cards, got %d instead",
                    EXPECTED_COMMUNITY_CARDS_COUNT, communityCards.size()
            ));
        }

        // TODO: Implement this function in the future after PokerHandCreator is implemented
        return null;
    }
}
