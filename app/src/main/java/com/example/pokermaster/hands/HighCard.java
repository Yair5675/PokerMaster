package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighCard implements PokerHand {
    private static final int HAND_RANK = 9;
    private final List<Card> mSortedCards;

    public HighCard(List<Card> rawHand) {
        mSortedCards = new ArrayList<>(rawHand);
        mSortedCards.sort(Comparator.comparing(card -> -card.getRank()));
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
        if (pokerHand instanceof HighCard) {
            final HighCard other = (HighCard) pokerHand;
            int comparison;
            for (int i = 0; i < mSortedCards.size(); i++) {
                comparison = Integer.compare(other.mSortedCards.get(i).getRank(), mSortedCards.get(i).getRank());
                if (comparison != 0)
                    return comparison;
            }
            return 0;
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
