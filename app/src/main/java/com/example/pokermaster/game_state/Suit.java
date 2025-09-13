package com.example.pokermaster.game_state;

/**
 * The suit of a poker card
 */
public enum Suit {
    SPADE,
    HEART,
    DIAMOND,
    CLUB
    ;

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
}
