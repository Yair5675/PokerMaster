package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

public class FullHouse implements PokerHand {
    private static final int HAND_RANK = 3;
    private final int mTripletRank;
    private final int mPairRank;

    public FullHouse(int tripletRank, int pairRank) {
        mTripletRank = tripletRank;
        mPairRank = pairRank;
    }

    public int getTripletRank() {
        return mTripletRank;
    }

    public int getPairRank() {
        return mPairRank;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof FullHouse) {
            final FullHouse other = (FullHouse) pokerHand;

            int comparison;
            // Order inverted intentionally! (our higher rank should make the result negative)
            if ((comparison = Integer.compare(other.mTripletRank, mTripletRank)) != 0)
                return comparison;
            return Integer.compare(other.mPairRank, mPairRank);
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
