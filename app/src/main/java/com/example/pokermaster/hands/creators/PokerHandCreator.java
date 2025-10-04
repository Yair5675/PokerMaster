package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.List;

/**
 * An interface responsible for creating a {@link PokerHand} from the "raw" hand (a list of cards)
 * and the {@link HandProperties properties} derived from it.
 */
public interface PokerHandCreator {
    /**
     * Creates a {@link PokerHand} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A {@link PokerHand} instance representing the given raw hand.
     * @throws PokerHandCreatorException If any error occurs when creating the hand, like properties
     *                                   that don't match the specific hand type.
     */
    PokerHand create(List<Card> rawHand, HandProperties properties) throws PokerHandCreatorException;

    // TODO: Make all implementations throw InvalidHandSizeException if the raw hand is not exactly
    //  5 cards
}
