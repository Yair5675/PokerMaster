package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;
import com.example.pokermaster.hands.Flush;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator class for the {@link Flush} hand.
 */
public class FlushCreator implements PokerHandCreator {
    /**
     * Creates a {@link Flush} instance from the given raw hand and properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A {@link Flush} instance.
     * @throws IndexOutOfBoundsException If no cards were given in {@code rawHand} (which should
     *                                   never occur!).
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) {
        final Suit matchingSuit = rawHand.get(0).getSuit();
        final List<Integer> cardRanks = rawHand
                .stream()
                .map(Card::getRank)
                .collect(Collectors.toList());
        return new Flush(cardRanks, matchingSuit);
    }
}
