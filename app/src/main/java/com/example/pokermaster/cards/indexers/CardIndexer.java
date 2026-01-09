package com.example.pokermaster.cards.indexers;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;

import java.util.Optional;

public class CardIndexer {
    private static final int POSSIBLE_RANK_VALUES_COUNT = Card.MAX_RANK - Card.MIN_RANK + 1;

    public int getCardIndex(Card card) {
        return card.getRank() - Card.MIN_RANK + card.getSuit().ordinal() * POSSIBLE_RANK_VALUES_COUNT;
    }

    public Optional<Card> getCardFromIndex(int index) {
        int rank = (index % POSSIBLE_RANK_VALUES_COUNT) + Card.MIN_RANK;
        // Rank calculation already guarantees rank < Card.MAX_RANK
        if (rank < Card.MIN_RANK)
            return Optional.empty();

        int suitIndex = index / POSSIBLE_RANK_VALUES_COUNT;
        Suit[] suits = Suit.values();
        if (suitIndex < 0 || suitIndex >= suits.length)
            return Optional.empty();

        return Optional.of(new Card(rank, suits[suitIndex]));
    }
}
