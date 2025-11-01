package com.example.pokermaster.cards;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Represents a single card in a poker game.
 */
public class Card implements Comparable<Card> {
    private static final String TAG = Card.class.getCanonicalName();

    public static final int MAX_RANK = 14;
    public static final int MIN_RANK = 2;
    public static final int ACE_RANK = 14;
    public static final int KING_RANK = 13;
    public static final int QUEEN_RANK = 12;
    public static final int JACK_RANK = 11;

    private static final String ACE_SYMBOL = "A";
    private static final String KING_SYMBOL = "K";
    private static final String QUEEN_SYMBOL = "Q";
    private static final String JACK_SYMBOL = "J";

    private static final String RANK_TAG = "<r>";
    private static final String SUIT_TAG = "<s>";
    private static final String TO_STRING_TEMPLATE = RANK_TAG + SUIT_TAG;

    private static final int RANK_HASH_BIT_COUNT = Integer.SIZE - Integer.numberOfLeadingZeros(MAX_RANK);
    private static final int RANK_HASH_MASK = (int) ((1L << RANK_HASH_BIT_COUNT) - 1);

    private static final int SPADE_HASH = 0b00;
    private static final int HEART_HASH = 0b01;
    private static final int DIAMOND_HASH = 0b10;
    private static final int CLUB_HASH = 0b11;

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
                return SPADE_HASH;
            case HEART:
                return HEART_HASH;
            case DIAMOND:
                return DIAMOND_HASH;
            case CLUB:
                return CLUB_HASH;
        }

        final String errorMessage = "Unknown suit was inserted to Card object";
        Log.e(TAG, "getSuitHashMask: " + errorMessage);
        throw new IllegalStateException(errorMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Card) {
            final Card otherCard = (Card) other;
            return otherCard.hashCode() == this.hashCode(); // Relies on unique hashCode property
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int suitHash = getSuitHashMask();
        return (mRank & RANK_HASH_MASK) | (suitHash << RANK_HASH_BIT_COUNT);
    }

    /**
     * Returns the visual representation of the current {@link Card card}'s rank.
     * <p>
     *     For ranks between 1 and 10 (inclusively), it is merely the number itself, but for all
     *     ranks between {@link Card#JACK_RANK Jack} and {@link Card#ACE_RANK} a special symbol
     *     is assigned.
     * </p>
     * @return The string representing the current card's rank.
     */
    private String getRankSymbol() {
        switch (mRank) {
            case ACE_RANK:
                return ACE_SYMBOL;
            case KING_RANK:
                return KING_SYMBOL;
            case QUEEN_RANK:
                return QUEEN_SYMBOL;
            case JACK_RANK:
                return JACK_SYMBOL;
            default:
                return Integer.toString(mRank);
        }
    }

    /**
     * {@code toString} implementation for the {@link Card} class, presenting a card in a pretty
     * representation.
     * @return String representation of the current card.
     */
    @NonNull
    @Override
    public String toString() {
        final String rankSymbol = getRankSymbol();
        final String suitSymbol = mSuit.toString();
        return TO_STRING_TEMPLATE.replace(RANK_TAG, rankSymbol).replace(SUIT_TAG, suitSymbol);
    }

    /**
     * Compares one {@link Card} instance to another.
     * <p>
     *     The comparison is made first based on the card's rank (ace high), and then on its
     *     {@link Suit suit} (based on it's {@link Enum#ordinal()} value).
     * </p>
     * @param other The card compared to this one.
     * @return An integer value which adheres to the {@link Comparable} interface's instructions.
     */
    @Override
    public int compareTo(@NonNull Card other) {
        final int rankComparison = Integer.compare(mRank, other.mRank);
        if (rankComparison != 0)
            return rankComparison;
        return Integer.compare(mSuit.ordinal(), other.mSuit.ordinal());
    }
}
