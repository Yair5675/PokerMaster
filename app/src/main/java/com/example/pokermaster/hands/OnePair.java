package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

import com.example.pokermaster.cards.Card;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OnePair implements PokerHand {
    private static final int HAND_RANK = 8;
    public static final int EXPECTED_KICKERS_COUNT = 3;

    private final int mPairRank;
    private final List<Integer> mSortedKickersRanks;
    private final List<Card> mRawHand;

    public OnePair(int pairRank, List<Integer> kickersRanks, List<Card> rawHand) {
        if (kickersRanks.size() != EXPECTED_KICKERS_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Expected %d kickers, got %d",
                    EXPECTED_KICKERS_COUNT, kickersRanks.size()
            ));
        }
        mPairRank = pairRank;
        mSortedKickersRanks = kickersRanks
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        mRawHand = rawHand;
    }

    public int getPairRank() {
        return mPairRank;
    }

    public List<Integer> getSortedKickersRanks() {
        return mSortedKickersRanks;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public List<Card> getCards() {
        return mRawHand;
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
            for (int i = 0; i < mSortedKickersRanks.size(); i++) {
                comparison = Integer.compare(other.mSortedKickersRanks.get(i), mSortedKickersRanks.get(i));
                if (comparison != 0)
                    return comparison;
            }
            return 0;
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
