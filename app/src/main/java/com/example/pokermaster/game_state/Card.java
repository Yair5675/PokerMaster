package com.example.pokermaster.game_state;

import android.util.Log;

/**
 * Represents a single card in a poker game.
 */
public class Card {
    private static final String TAG = Card.class.getCanonicalName();

    public static final int MAX_RANK = 52;
    public static final int MIN_RANK = 1;
    private static final int RANK_HASH_BIT_COUNT = Integer.SIZE - Integer.numberOfLeadingZeros(MAX_RANK);
    private static final int RANK_HASH_MASK = (int) ((1L << RANK_HASH_BIT_COUNT) - 1);

    private static final int SPADE_HASH_MASK = 0b00;
    private static final int HEART_HASH_MASK = 0b01;
    private static final int DIAMOND_HASH_MASK = 0b10;
    private static final int CLUB_HASH_MASK = 0b11;

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

    /**
     * Retrieves the hash mask mapped to the current card's {@link Suit}.
     * @return The bits used when hashing the suit of the current card.
     * @throws IllegalStateException If somehow the suit of the current card does not have a hash
     *                               mask (should never happen but let's crash for fun if it does).
     */
    private int getSuitHashMask() {
        switch (mSuit) {
            case SPADE:
                return SPADE_HASH_MASK;
            case HEART:
                return HEART_HASH_MASK;
            case DIAMOND:
                return DIAMOND_HASH_MASK;
            case CLUB:
                return CLUB_HASH_MASK;
        }

        final String errorMessage = "Unknown suit was inserted to Card object";
        Log.e(TAG, "getSuitHashMask: " + errorMessage);
        throw new IllegalStateException(errorMessage);
    }

    @Override
    public int hashCode() {
        final int suitHash = getSuitHashMask();
        return (mRank & RANK_HASH_MASK) | (suitHash << RANK_HASH_BIT_COUNT);
    }
}
