package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Flush implements PokerHand {
    private static final int HAND_RANK = 4;
    public static final int EXPECTED_CARDS_COUNT = 5;
    private final Suit mMatchingSuit;
    private final List<Integer> mSortedCardsRanks;

    public Flush(List<Integer> cardRanks, Suit matchingSuit) {
        if (cardRanks.size() != EXPECTED_CARDS_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "Expected %d cards, got %d instead", EXPECTED_CARDS_COUNT, cardRanks.size()
            ));
        }

        mSortedCardsRanks = cardRanks
                .stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
        mMatchingSuit = matchingSuit;
    }

    public Suit getMatchingSuit() {
        return mMatchingSuit;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public int compareTo(PokerHand pokerHand) {
        if (this == pokerHand)
            return 0;
        if (pokerHand instanceof Flush) {
            final Flush other = (Flush) pokerHand;
            int comparison;
            for (int i = 0; i < mSortedCardsRanks.size(); i++) {
                // Order inverted intentionally! (our higher rank should make the result negative)
                comparison = Integer.compare(other.mSortedCardsRanks.get(i), mSortedCardsRanks.get(i));
                if (comparison != 0)
                    return comparison;
            }
            return 0;
        }
        return Integer.compare(getHandRanking(), pokerHand.getHandRanking());
    }

    @Override
    public List<Card> getCards() {
        return List.of(
                new Card(mSortedCardsRanks.get(0), mMatchingSuit),
                new Card(mSortedCardsRanks.get(1), mMatchingSuit),
                new Card(mSortedCardsRanks.get(2), mMatchingSuit),
                new Card(mSortedCardsRanks.get(3), mMatchingSuit),
                new Card(mSortedCardsRanks.get(4), mMatchingSuit)
        );
    }
}
