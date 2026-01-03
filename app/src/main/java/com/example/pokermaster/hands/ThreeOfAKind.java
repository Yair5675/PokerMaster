package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

import com.example.pokermaster.cards.Card;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ThreeOfAKind implements PokerHand {
    private static final int HAND_RANK = 6;

    private final int mMatchingCardRank;
    private final int mLowKickerRank;
    private final int mHighKickerRank;
    private final List<Card> mRawHand;

    public ThreeOfAKind(int matchingCardRank, int kicker1, int kicker2, List<Card> rawHand) {
        mMatchingCardRank = matchingCardRank;
        mLowKickerRank = Math.min(kicker1, kicker2);
        mHighKickerRank = Math.max(kicker1, kicker2);
        mRawHand = rawHand;
    }

    public int getHighKickerRank() {
        return mHighKickerRank;
    }

    public int getLowKickerRank() {
        return mLowKickerRank;
    }

    public int getMatchingCardRank() {
        return mMatchingCardRank;
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
        if (pokerHand instanceof ThreeOfAKind) {
            final ThreeOfAKind other = (ThreeOfAKind) pokerHand;
            // Compare matching card first, then high kicker, then low kicker:
            final List<Function<ThreeOfAKind, Integer>> gettersToCompare = List.of(
                    ThreeOfAKind::getMatchingCardRank,
                    ThreeOfAKind::getHighKickerRank,
                    ThreeOfAKind::getLowKickerRank
            );
            for (Function<ThreeOfAKind, Integer> getterToCompare : gettersToCompare) {
                final int comparison = Integer.compare(
                        // Order inverted intentionally! (our higher rank should make the result negative)
                        getterToCompare.apply(other), getterToCompare.apply(this)
                );
                if (comparison != 0)
                    return comparison;
            }
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
