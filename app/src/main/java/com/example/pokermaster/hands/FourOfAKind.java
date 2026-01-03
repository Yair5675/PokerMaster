package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

import com.example.pokermaster.cards.Card;

import java.util.List;

public class FourOfAKind implements PokerHand {
    private static final int HAND_RANK = 2;
    private final int mMatchingCardRank;
    private final int mKickerRank;
    private final List<Card> mRawHand;

    public FourOfAKind(int matchingCardRank, int kickerRank, List<Card> rawHand) {
        mMatchingCardRank = matchingCardRank;
        mKickerRank = kickerRank;
        mRawHand = rawHand;
    }

    public int getMatchingCardRank() {
        return mMatchingCardRank;
    }

    public int getKickerRank() {
        return mKickerRank;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof FourOfAKind) {
            FourOfAKind other = (FourOfAKind) pokerHand;
            final int matchingCardComparison = Integer.compare(
                    // Order inverted intentionally! (our higher rank should make the result negative)
                    other.mMatchingCardRank, mMatchingCardRank
            );
            if (matchingCardComparison != 0)
                return matchingCardComparison;

            return Integer.compare(mKickerRank, other.mKickerRank);
        }

        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }

    @Override
    public List<Card> getCards() {
        return mRawHand;
    }
}
