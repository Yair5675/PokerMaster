package com.example.pokermaster.cards.indexers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;


public class CardIndexerTest {
    private static final CardIndexer cardIndexer = new CardIndexer();

    @Test
    public void testCardIndexer_sanity() {
        Card testedCard;
        for (int rank = Card.MIN_RANK; rank <= Card.MAX_RANK; rank++) {
            for (Suit suit : Suit.values()) {
                testedCard = new Card(rank, suit);

                int cardIndex = cardIndexer.getCardIndex(testedCard);
                Optional<Card> calculatedCard = cardIndexer.getCardFromIndex(cardIndex);

                assertTrue(calculatedCard.isPresent());
                assertEquals(testedCard, calculatedCard.get());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MAX_VALUE, Integer.MIN_VALUE})
    public void testGetCardFromIndex_ReturnsEmptyOptional_WhenGivenInvalidIndex(int invalidIndex) {
        Optional<Card> card = cardIndexer.getCardFromIndex(invalidIndex);
        assertTrue(card.isEmpty());
    }
}
