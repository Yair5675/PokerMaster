package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

/**
 * The best hand in poker - the Royal Flush.
 * If two players have a royal flush it's always a draw, so this class is a singleton.
 */
public class RoyalFlush implements PokerHand {
    private static final int HAND_RANK = 0;
    private static RoyalFlush sInstance = null;

    private RoyalFlush() {}

    public static RoyalFlush getInstance() {
        if (sInstance == null)
            sInstance = new RoyalFlush();
        return sInstance;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        return -1;
    }
}
