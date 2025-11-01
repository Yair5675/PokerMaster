package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.HighCard;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.creators.exceptions.InvalidHandSizeException;

import java.util.List;

/**
 * A creator class for the {@link HighCard} poker hand.
 */
public class HighCardCreator implements PokerHandCreator {
    /**
     * Creates a {@link HighCard} hand from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A new {@link HighCard} instance.
     * @throws InvalidHandSizeException If the given raw hand does not contain exactly
     *                                  {@link PokerHand#HAND_SIZE} cards.
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) throws InvalidHandSizeException {
        if (rawHand.size() != PokerHand.HAND_SIZE)
            throw new InvalidHandSizeException(String.format(
                    "Expected %d cards in raw hand, got %d", PokerHand.HAND_SIZE, rawHand.size()
            ));
        return new HighCard(
                rawHand.get(0).getRank(),
                rawHand.get(1).getRank(),
                rawHand.get(2).getRank(),
                rawHand.get(3).getRank(),
                rawHand.get(4).getRank()
        );
    }
}
