package com.example.pokermaster.hands;

public class FourOfAKind implements PokerHand {
    private static final int HAND_RANK = 2;
    private final int mMatchingCardRank;
    private final int mKickerRank;

    public FourOfAKind(int matchingCardRank, int kickerRank) {
        mMatchingCardRank = matchingCardRank;
        mKickerRank = kickerRank;
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
    public int compareTo(PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof FourOfAKind) {
            FourOfAKind other = (FourOfAKind) pokerHand;
            final int matchingCardComparison = Integer.compare(
                    mMatchingCardRank, other.mMatchingCardRank
            );
            if (matchingCardComparison != 0)
                return matchingCardComparison;

            return Integer.compare(mKickerRank, other.mKickerRank);
        }

        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
