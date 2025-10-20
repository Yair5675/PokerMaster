package com.example.pokermaster.hands.selectors;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.HoleCards;
import com.example.pokermaster.hands.PokerHand;

import java.util.List;

/**
 * An interface responsible for selecting the best combination of hole and community cards.
 * <p>
 *     This is an interface solely due to the fact that many optimizations can be applied to this
 *     process, so it must be extensible/easily replaceable.
 * </p>
 */
public interface BestHandSelector {
    /**
     * Number of expected community cards, every implementation should enforce that the length of
     * {@code communityCards} equals this constant.
     */
    int EXPECTED_COMMUNITY_CARDS_COUNT = 5;

    /**
     * Given the {@link HoleCards} of a player and ALL FIVE community cards, the function outputs
     * the best {@link PokerHand} that can be made from 5 of the 7 aforementioned cards.
     * Note that the hole cards may not be used in the final poker hand if the community cards alone
     * offer a better combination, and using the hole cards would worsen it.
     * @param holeCards The two private cards that belong to a player.
     * @param communityCards The 5 public cards which will be combined
     * @return The best poker hand that can be made from the given cards.
     * @throws IllegalArgumentException Every implementation should throw this exception if the
     *                                  number of community cards given isn't
     *                                  {@link BestHandSelector#EXPECTED_COMMUNITY_CARDS_COUNT}.
     */
    PokerHand getBestHand(HoleCards holeCards, List<Card> communityCards);
}
