package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.Straight;

import java.util.List;

/**
 * Creator class for the {@link Straight} hand.
 */
public class StraightCreator implements PokerHandCreator {
    /**
     * Creates a {@link Straight} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A new {@link Straight} instance.
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) {
        return new Straight(properties.getHighCardRank());
    }
}
