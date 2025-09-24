package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class OnePair implements PokerHand {
    private static final int HAND_RANK = 8;
    public static final int EXPECTED_KICKERS_COUNT = 3;

    private final int mPairRank;
    private final int[] mSortedKickersRanks;

    public OnePair(int pairRank, int[] sortedKickersRanks) {
        if (sortedKickersRanks.length != EXPECTED_KICKERS_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Expected %d kickers, got %d",
                    EXPECTED_KICKERS_COUNT, sortedKickersRanks.length
            ));
        }
        mPairRank = pairRank;
        mSortedKickersRanks = Arrays.copyOf(sortedKickersRanks, EXPECTED_KICKERS_COUNT);
        sortKickersReversed();
    }

    private void sortKickersReversed() {
        Arrays.sort(mSortedKickersRanks);
        final int length = mSortedKickersRanks.length;
        for (int i = 0; i < length / 2; i++) {
            mSortedKickersRanks[i] = mSortedKickersRanks[length - i - 1];
        }
    }

    public int getPairRank() {
        return mPairRank;
    }

    public int[] getSortedKickersRanks() {
        return mSortedKickersRanks;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof OnePair) {
            final OnePair other = (OnePair) pokerHand;

            int comparison;
            // Order inverted intentionally! (our higher rank should make the result negative)
            if ((comparison = Integer.compare(other.mPairRank, mPairRank)) != 0)
                return comparison;
            for (int i = 0; i < mSortedKickersRanks.length; i++) {
                comparison = Integer.compare(other.mSortedKickersRanks[i], mSortedKickersRanks[i]);
                if (comparison != 0)
                    return comparison;
            }
            return 0;
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
