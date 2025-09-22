package com.example.pokermaster.hands;

/**
 * An abstraction over the type of hands in Texas hold 'em Poker.
 * Note that when comparing two hands, the better one will be considered SMALLER (and its compareTo
 * function will return a negative value).
 */
public interface PokerHand extends Comparable<PokerHand> {
    /**
     * Returns the ranking of the hand's TYPE.
     * The implementation should be static (and ideally constant).
     * @return A number indicating the rank of the implementing poker hand class.
     */
    int getHandRanking();
}
