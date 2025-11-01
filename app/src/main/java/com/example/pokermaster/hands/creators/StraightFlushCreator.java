package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.StraightFlush;

import java.util.List;

/**
 * A creator class for the {@link StraightFlush} hand.
 */
public class StraightFlushCreator implements PokerHandCreator {
    /**
     * Creates a {@link StraightFlush} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A {@link StraightFlush} instance.
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) {
        return new StraightFlush(properties.getHighCardRank());
    }
}
