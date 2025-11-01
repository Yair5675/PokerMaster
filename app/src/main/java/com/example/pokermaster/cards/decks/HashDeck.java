package com.example.pokermaster.cards.decks;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;
import com.example.pokermaster.cards.exceptions.EmptyDeckException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * A deck using the cards' hashing to store them.
 */
public class HashDeck implements Deck {
    private final HashSet<Card> mCards;

    /**
     * Creates a new HashDeck with all cards already in it.
     */
    public HashDeck() {
        final Suit[] suits = Suit.values();
        final int allCardsCount = (Card.MAX_RANK - Card.MIN_RANK) * suits.length;
        mCards = new HashSet<>(allCardsCount);

        for (int rank = Card.MIN_RANK; rank <= Card.MAX_RANK; rank++) {
            for (Suit suit : suits) {
                mCards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Creates a deck populated with the given cards.
     * Duplicate cards CAN be passed, but will be added to the deck only once.
     * @param cards The cards included in the deck.
     */
    public HashDeck(Card ... cards) {
        mCards = new HashSet<>(Arrays.asList(cards));
    }

    /**
     * Creates a deck from the given cards set.
     * @param cards A set of card which will be used to create a Deck.
     */
    public HashDeck(Set<Card> cards) {
        mCards = new HashSet<>(cards);
    }

    @Override
    public boolean isEmpty() {
        return mCards.isEmpty();
    }

    @Override
    public int getCardsCount() {
        return mCards.size();
    }

    @Override
    public boolean isInDeck(Card card) {
        return mCards.contains(card);
    }

    @Override
    public boolean addToDeck(Card card) {
        return !mCards.add(card);
    }

    @Override
    public Card popRandomCard() throws EmptyDeckException {
        final Random random = new Random();

        // Random selection is O(n) which is pretty bad, but the maximum deck size is minimal
        // and honestly I couldn't be bothered.
        final int randomCardIndex = random.nextInt(getCardsCount());
        final Optional<Card> optionalRandomCard = mCards.stream().skip(randomCardIndex).findFirst();
        if (optionalRandomCard.isEmpty()) {
            throw new EmptyDeckException();
        }

        final Card randomCard = optionalRandomCard.get();
        mCards.remove(randomCard);
        return randomCard;
    }

    @Override
    public boolean removeFromDeck(Card card) {
        return mCards.remove(card);
    }
}
