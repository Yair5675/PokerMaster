package com.example.pokermaster.hands;

import androidx.annotation.NonNull;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;

import java.util.Collections;
import java.util.List;

/**
 * The best hand in poker - the Royal Flush.
 * If two players have a royal flush it's always a draw, so this class is a singleton.
 */
public class RoyalFlush implements PokerHand {
    private static final int HAND_RANK = 0;
    private final Suit mSuit;

    public RoyalFlush(Suit suit) {
        mSuit = suit;
    }

    @Override
    public int getHandRanking() {
        return HAND_RANK;
    }

    @Override
    public List<Card> getCards() {
        return List.of(
                new Card(Card.ACE_RANK, mSuit),
                new Card(Card.KING_RANK, mSuit),
                new Card(Card.QUEEN_RANK, mSuit),
                new Card(Card.JACK_RANK, mSuit),
                new Card(10, mSuit)
        );
    }

    @Override
    public int compareTo(@NonNull PokerHand pokerHand) {
        if (pokerHand instanceof RoyalFlush)
            return 0;
        return -1;
    }
}
