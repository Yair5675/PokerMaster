package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import org.junit.Test;
import static org.junit.Assert.*;


public class PokerHandFactoryTest {
    /**
     * Utility function to easily create cards from string.
     * It assumes the input is always correct, and is the card's rank followed by its suit.
     */
    private static Card card(String card) {
        final char[] cardCharacters = card.toCharArray();
        final String suitSymbol = Character.toString(cardCharacters[cardCharacters.length - 1]);
        Suit suit = null;
        for (Suit maybeSuit : Suit.values()) {
            if (maybeSuit.toString().equals(suitSymbol)) {
                suit = maybeSuit;
                break;
            }
        }

        int rank = -1;
        if ('1' <= cardCharacters[0] && cardCharacters[0] <= '9')
            rank = Integer.parseInt(card, 0, cardCharacters.length - 1, 10);
        else if (cardCharacters[0] == 'A')
            rank = Card.ACE_RANK;
        else if (cardCharacters[0] == 'K')
            rank = Card.KING_RANK;
        else if (cardCharacters[0] == 'Q')
            rank = Card.QUEEN_RANK;
        else if (cardCharacters[0] == 'J')
            rank = Card.JACK_RANK;

        if (suit == null || rank < Card.MIN_RANK)
            throw new IllegalArgumentException("Invalid card string given: " + card);
        return new Card(rank, suit);
    }


    @Test
    public void testHighCardSanity() throws PokerHandCreatorException {
        final Card[][] highCardHands = {
                { card("A♠"),  card("9♥"),  card("7♦"),  card("4♣"),  card("2♠") },
                { card("K♦"),  card("J♣"),  card("9♠"),  card("6♥"),  card("3♦") },
                { card("Q♣"),  card("10♦"), card("8♠"),  card("5♥"),  card("2♣") },
                { card("J♦"),  card("9♣"),  card("7♥"),  card("4♠"),  card("3♦") },
                { card("10♠"), card("8♦"),  card("6♥"),  card("4♣"),  card("2♦") },
                { card("9♥"),  card("7♠"),  card("5♦"),  card("3♣"),  card("2♥") },
                { card("A♦"),  card("Q♠"),  card("8♥"),  card("5♣"),  card("3♦") },
                { card("K♣"),  card("10♥"), card("9♦"),  card("6♠"),  card("4♥") },
                { card("Q♦"),  card("J♠"),  card("8♣"),  card("6♥"),  card("3♠") },
                { card("J♣"),  card("9♦"),  card("7♠"),  card("5♥"),  card("2♣") }
        };

        for (Card[] hand : highCardHands) {
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not High Card", bestHand instanceof HighCard);
        }
    }
}
