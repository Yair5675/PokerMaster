package com.example.pokermaster.game_state;

/**
 * Represents a single card in a poker game.
 */
public class Card {
    public static final int MAX_RANK = 52;
    public static final int MIN_RANK = 1;

    private final int mRank;
    private final Suit mSuit;

    public Card(int rank, Suit suit) {
        if (rank > MAX_RANK || rank < MIN_RANK) {
            throw new IllegalArgumentException(String.format(
                    "Invalid rank, must be between %d and %d inclusively (got %d)",
                    MIN_RANK, MAX_RANK, rank
            ));
        }

        this.mRank = rank;
        this.mSuit = suit;
    }

    public int getRank() {
        return mRank;
    }

    public Suit getSuit() {
        return mSuit;
    }
}
