package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

public class StraightFlush implements PokerHand {
    private static final int HAND_RANK = 1;
    private final int mHighestCardRank;

    public StraightFlush(int highestCardRank) {
        mHighestCardRank = highestCardRank;
    }

    public int getHighestCardRank() {
        return mHighestCardRank;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof StraightFlush) {
            final StraightFlush other = (StraightFlush) pokerHand;
            // Order inverted intentionally! (our higher rank should make the result negative)
            return Integer.compare(other.mHighestCardRank, mHighestCardRank);
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
