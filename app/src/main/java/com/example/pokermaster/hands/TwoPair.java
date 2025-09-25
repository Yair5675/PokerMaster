package com.example.pokermaster.hands;

import java.util.List;
import java.util.function.Function;

public class TwoPair implements PokerHand {
    private static final int HAND_RANK = 7;
    private final int mHighPairRank;
    private final int mLowPairRank;
    private final int mKickerRank;

    public TwoPair(int firstPairRank, int secondPairRank, int kickerRank) {
        mHighPairRank = Math.max(firstPairRank, secondPairRank);
        mLowPairRank = Math.min(firstPairRank, secondPairRank);
        mKickerRank = kickerRank;
    }

    public int getHighPairRank() {
        return mHighPairRank;
    }

    public int getLowPairRank() {
        return mLowPairRank;
    }

    public int getKickerRank() {
        return mKickerRank;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof TwoPair) {
            final TwoPair other = (TwoPair) pokerHand;
            int comparison;
            final List<Function<TwoPair, Integer>> gettersToCompare = List.of(
                    TwoPair::getHighPairRank,
                    TwoPair::getLowPairRank,
                    TwoPair::getKickerRank
            );
            for (Function<TwoPair, Integer> getterToCompare : gettersToCompare) {
                // Order inverted intentionally! (our higher rank should make the result negative)
                comparison = Integer.compare(
                        getterToCompare.apply(other), getterToCompare.apply(this)
                );
                if (comparison != 0)
                    return comparison;
            }
            return 0;
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
