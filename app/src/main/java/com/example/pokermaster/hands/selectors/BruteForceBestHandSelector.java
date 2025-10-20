package com.example.pokermaster.hands.selectors;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.HoleCards;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.PokerHandFactory;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;
import com.example.pokermaster.hands.selectors.exceptions.BestHandSelectorException;

import java.util.List;

/**
 * Implementation of {@link BestHandSelector} that computes the best hand for every combination of
 * 5 cards out of the 7 given to it, then compares them all.
 * <p>
 *     This is by far the most naive and intuitive approach, yet should not be used as it is also
 *     the slowest.
 * </p>
 */
public class BruteForceBestHandSelector implements BestHandSelector {
    /**
     * Implementation of {@link BestHandSelector#getBestHand(HoleCards, List)} that iterates through
     * every combination of 5 cards out of the given seven and compares the best hand of each until
     * it finds the absolute best.
     * @param holeCards The two private cards that belong to a player.
     * @param communityCards The 5 public cards which will be combined
     * @return The best poker hand that can be made out of the given 7.
     * @throws IllegalArgumentException If the number of community cards given isn't
     *                                  {@link BestHandSelector#EXPECTED_COMMUNITY_CARDS_COUNT}.
     * @throws BestHandSelectorException If creating a poker hand out of any of the 5-card
     *                                   combination results in a {@link PokerHandCreatorException}
     */
    @Override
    public PokerHand getBestHand(HoleCards holeCards, List<Card> communityCards) throws BestHandSelectorException {
        if (communityCards.size() != EXPECTED_COMMUNITY_CARDS_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Expected %d community cards, got %d instead",
                    EXPECTED_COMMUNITY_CARDS_COUNT, communityCards.size()
            ));
        }

        // I do sincerely apologize for this monstrosity, but I couldn't be bothered doing something
        // fancy for a one-time-use class which is literally called "BruteForce"
        Card[] cards = {
                communityCards.get(0),
                communityCards.get(1),
                communityCards.get(2),
                communityCards.get(3),
                communityCards.get(4),
                holeCards.getFirstCard(),
                holeCards.getSecondCard()
        };
        PokerHand bestHand = null, currentHand;
        for (int card1 = 0; card1 < cards.length; card1++) {
            for (int card2 = card1 + 1; card2 < cards.length; card2++) {
                for (int card3 = card2 + 1; card3 < cards.length; card3++) {
                    for (int card4 = card3 + 1; card4 < cards.length; card4++) {
                        for (int card5 = card4 + 1; card5 < cards.length; card5++) {
                            try {
                                currentHand = PokerHandFactory.createBestHand(
                                        cards[card1],
                                        cards[card2],
                                        cards[card3],
                                        cards[card4],
                                        cards[card5]
                                );
                            } catch (PokerHandCreatorException creationException) {
                                throw new BestHandSelectorException(creationException);
                            }
                            if (bestHand == null || currentHand.isBetterThan(bestHand))
                                bestHand = currentHand;
                        }
                    }
                }
            }
        } // Please don't ridicule this project (ಥ _ ಥ)

        return bestHand;
    }
}
