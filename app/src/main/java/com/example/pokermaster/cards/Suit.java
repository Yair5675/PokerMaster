package com.example.pokermaster.cards;

import androidx.annotation.NonNull;

/**
 * The suit of a poker card
 */
public enum Suit {
    SPADE,
    HEART,
    DIAMOND,
    CLUB
    ;

    private static final String SPADE_SYMBOL = "♠";
    private static final String HEART_SYMBOL = "♥";
    private static final String DIAMOND_SYMBOL = "♦";
    private static final String CLUB_SYMBOL = "♣";

    /**
     * Checks if the given suit is a black suit (for example - SPADE).
     * @return True if the current suit is a black suit.
     */
    public boolean isBlack() {
        return this == SPADE || this == CLUB;
    }

    /**
     * Checks if the given suit is a red suit (for example - heart).
     * @return True if the current suit is a red suit.
     */
    public boolean isRed() {
        return this == HEART || this == DIAMOND;
    }

    /**
     * {@code toString} implementation for the {@link Suit} class, providing a visually appealing
     * representation of all suits.
     * @return The current {@link Suit} variant's pretty string representation.
     */
    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case SPADE:
                return SPADE_SYMBOL;
            case HEART:
                return HEART_SYMBOL;
            case DIAMOND:
                return DIAMOND_SYMBOL;
            case CLUB:
                return CLUB_SYMBOL;
        }
        throw new IllegalStateException("toString not implemented for a Suit enum variant");
    }
}
