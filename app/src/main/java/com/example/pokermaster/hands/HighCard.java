package com.example.pokermaster.hands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighCard implements PokerHand {
    private static final int HAND_RANK = 9;
    private final List<Integer> mSortedRanks;

    public HighCard(int rank1, int rank2, int rank3, int rank4, int rank5) {
        mSortedRanks = new ArrayList<>(List.of(rank1, rank2, rank3, rank4, rank5));
        mSortedRanks.sort(Comparator.reverseOrder());
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof HighCard) {
            final HighCard other = (HighCard) pokerHand;
            int comparison;
            for (int i = 0; i < mSortedRanks.size(); i++) {
                comparison = Integer.compare(other.mSortedRanks.get(i), mSortedRanks.get(i));
                if (comparison != 0)
                    return comparison;
            }
            return 0;
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
