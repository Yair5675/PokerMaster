package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Straight implements PokerHand {
    private static final int HAND_RANK = 5;
    private final int mHighestCardRank;
    private final List<Card> mSortedCards;

    public Straight(List<Card> rawHand) {
        mSortedCards = new ArrayList<>(rawHand);
        mSortedCards.sort(Comparator.comparing((card -> -card.getRank())));
        // If we have a low-ace straight, make sure not to pick the ace (its rank should be treated
        // as a '1'):
        if (mSortedCards.get(1).getRank() == 5 && mSortedCards.get(0).getRank() == Card.ACE_RANK) {
            mHighestCardRank = 5;
        } else {
            mHighestCardRank = mSortedCards.get(0).getRank();
        }
    }

    public int getHighestCardRank() {
        return mHighestCardRank;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public List<Card> getCards() {
        return mSortedCards;
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
