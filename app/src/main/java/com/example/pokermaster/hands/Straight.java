package com.example.pokermaster.hands;

public class Straight implements PokerHand {
    private static final int HAND_RANK = 5;
    private final int mHighestCardRank;

    public Straight(int highestCardRank) {
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
    public int compareTo(PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof Straight) {
            // Order inverted intentionally! (our higher rank should make the result negative)
            return Integer.compare(((Straight) pokerHand).mHighestCardRank, mHighestCardRank);
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
