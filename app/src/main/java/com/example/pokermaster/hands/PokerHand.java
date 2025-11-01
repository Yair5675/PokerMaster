package com.example.pokermaster.hands;

/**
 * An abstraction over the type of hands in Texas hold 'em Poker.
 * <p>
 * Note that when comparing two hands, the better one will be considered SMALLER (and its
 * {@link Comparable<PokerHand>#compareTo} function will return a negative value).
 * </p>
 */
public interface PokerHand extends Comparable<PokerHand> {
    /**
     * Number of cards in a single hand
     */
    int HAND_SIZE = 5;

    /**
     * Returns the ranking of the hand's TYPE.
     * The implementation should be static (and ideally constant).
     * @return A number indicating the rank of the implementing poker hand class.
     */
    int getHandRanking();

    /**
     * Checks if this hand is better than the given one.
     * <p>
     *     This function is provided to avoid confusion regarding the {@link Comparable<PokerHand>}
     *     implementation of {@link PokerHand}. It can be replaced entirely by checking the
     *     result of {@link Comparable<PokerHand>#compareTo}.
     * </p>
     * @param other Another hand which will be compared to the current one.
     * @return True if the current hand is strictly better than the other (tying hands will return
     *         false).
     */
    default boolean isBetterThan(PokerHand other) {
        return this.compareTo(other) < 0;
    }
}
