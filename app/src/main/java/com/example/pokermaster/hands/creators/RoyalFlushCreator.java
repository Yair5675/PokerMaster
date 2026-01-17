package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.RoyalFlush;

import java.util.List;

/**
 * A creator class for the {@link RoyalFlush} hand.
 */
public class RoyalFlushCreator implements PokerHandCreator {
    /**
     * Creates a {@link RoyalFlush} instance from the given raw hand and hand properties.
     * <p>
     * In actuality, both parameters aren't used due to the implementation of {@link RoyalFlush} as
     * a singleton, but they are declared anyway to match {@link PokerHandCreator}'s signature.
     * </p>
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A {@link RoyalFlush} instance.
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) {
        final Suit royalFlushSuit = rawHand.get(0).getSuit();
        return new RoyalFlush(royalFlushSuit);
    }
}
