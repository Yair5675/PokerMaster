package com.example.pokermaster.cards.decks;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.HoleCards;
import com.example.pokermaster.cards.exceptions.EmptyDeckException;

/**
 * Abstraction over a full deck of cards.
 */
public interface Deck {

    /**
     * Checks if the deck ran out of cards.
     * @return True if no cards exist in the deck.
     */
    boolean isEmpty();

    /**
     * Returns the number of cards currently in the deck.
     * @return Number of cards in the deck.
     */
    int getCardsCount();

    /**
     * Checks if a given card is in the deck.
     * @param card Some card which may or may not be in the deck.
     * @return True if the given card (or a card with the same rank and suit) is in the deck.
     */
    boolean isInDeck(Card card);

    /**
     * Checks if both hole cards are in the deck.
     * This is more of a helper method to save the caller a line of code.
     * @param holeCards A pair of cards given to a player.
     * @return True if BOTH hole cards are in the deck.
     */
    default boolean isInDeck(HoleCards holeCards) {
        return isInDeck(holeCards.getFirstCard()) && isInDeck(holeCards.getSecondCard());
    }

    /**
     * Adds the given card to the deck.
     * If the card was already in the deck, the implementing class should NOT throw an exception but
     * instead return true.
     * @param card A card which will be added to the deck.
     * @return True if the card was already in the deck.
     */
    boolean addToDeck(Card card);

    /**
     * Adds both hole cards to the deck.
     * This is more of a helper method to save the caller a line of code.
     * @param holeCards A pair of cards given to a player.
     * @apiNote This method does not indicate whether either one (or both) of the cards were already
     *          in the deck. It simply ensures they exist in the deck AFTER the call to this method.
     */
    default void addToDeck(HoleCards holeCards) {
        addToDeck(holeCards.getFirstCard());
        addToDeck(holeCards.getSecondCard());
    }

    /**
     * Extracts a random card from the deck and returns it.
     * @return A randomly chosen card from the deck.
     * @throws EmptyDeckException If the deck was empty when calling this method.
     */
    Card popRandomCard() throws EmptyDeckException;

    /**
     * Removes a given card from the deck.
     * Implementing classes should not throw an exception if the card was not in the deck in the
     * first place, but instead return false..
     * @param card The card which will be removed from the deck.
     * @return True if the card previously existed in the deck and removed, false if the card was
     *         not in the deck in the first place.
     */
    boolean removeFromDeck(Card card);

    /**
     * Removes both hole cards from the deck.
     * This is more of a helper method to save the caller a line of code.
     * @param holeCards A pair of cards given to a player.
     * @apiNote This method does not indicate whether either one (or both) of the cards existed in
     *          the deck prior to the removal. It simply ensures they do not exist in the deck AFTER
     *          the call to this method.
     */
    default void removeFromDeck(HoleCards holeCards) {
        removeFromDeck(holeCards.getFirstCard());
        removeFromDeck(holeCards.getSecondCard());
    }
}
