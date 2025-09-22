package com.example.pokermaster.hands;

/**
 * An abstraction over the type of hands in Texas hold 'em Poker.
 */
public interface PokerHand extends Comparable<PokerHand> {
    /**
     * Returns the ranking of the hand's TYPE.
     * The implementation should be static (and ideally constant).
     * @return A number indicating the rank of the implementing poker hand class.
     */
    int getHandRanking();
}
