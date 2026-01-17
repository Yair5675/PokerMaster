package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

import com.example.pokermaster.cards.Card;

import java.util.List;

public class StraightFlush implements PokerHand {
    private static final int HAND_RANK = 1;
    private final Card mHighestCard;

    public StraightFlush(Card highestCard) {
        mHighestCard = highestCard;
    }

    public int getHighestCardRank() {
        return mHighestCard.getRank();
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public List<Card> getCards() {
        // Watch out for low-ace straight flushes!
        Card lowestCard;
        if (mHighestCard.getRank() == 5)
            lowestCard = new Card(Card.ACE_RANK, mHighestCard.getSuit());
        else
            lowestCard = new Card(mHighestCard.getRank() - 4, mHighestCard.getSuit());

        return List.of(
                mHighestCard,
                new Card(mHighestCard.getRank() - 1, mHighestCard.getSuit()),
                new Card(mHighestCard.getRank() - 2, mHighestCard.getSuit()),
                new Card(mHighestCard.getRank() - 3, mHighestCard.getSuit()),
                lowestCard
        );
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof StraightFlush) {
            final StraightFlush other = (StraightFlush) pokerHand;
            // Order inverted intentionally! (our higher rank should make the result negative)
            return Integer.compare(other.mHighestCard.getRank(), mHighestCard.getRank());
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }
}
