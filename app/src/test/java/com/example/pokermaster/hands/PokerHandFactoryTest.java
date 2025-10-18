package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;


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

    @Test
    public void testOnePairSanity() throws PokerHandCreatorException {
        final Card[][] onePairHands = {
            { card("A♠"),  card("A♦"),  card("9♥"),  card("6♣"),  card("3♠") }, // Pair of Aces
            { card("K♣"),  card("K♥"),  card("10♦"), card("8♠"),  card("4♣") }, // Pair of Kings
            { card("Q♦"),  card("Q♠"),  card("9♣"),  card("5♥"),  card("2♦") }, // Pair of Queens
            { card("J♥"),  card("J♣"),  card("8♦"),  card("6♠"),  card("3♥") }, // Pair of Jacks
            { card("10♣"), card("10♠"), card("7♦"),  card("5♥"),  card("4♠") }, // Pair of Tens
            { card("9♦"),  card("9♠"),  card("8♣"),  card("6♥"),  card("2♦") }, // Pair of Nines
            { card("8♠"),  card("8♦"),  card("7♥"),  card("5♣"),  card("3♠") }, // Pair of Eights
            { card("7♣"),  card("7♠"),  card("9♦"),  card("6♥"),  card("4♣") }, // Pair of Sevens
            { card("6♦"),  card("6♠"),  card("10♥"), card("8♣"),  card("2♥") }, // Pair of Sixes
            { card("5♣"),  card("5♦"),  card("9♠"),  card("7♥"),  card("3♣") }  // Pair of Fives
        };
        final int[] pairRanks = {
                Card.ACE_RANK, Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10, 9, 8, 7, 6, 5
        };
        final List<List<Integer>> sortedKickersRanks = List.of(
                List.of(9, 6, 3),
                List.of(10, 8, 4),
                List.of(9, 5, 2),
                List.of(8, 6, 3),
                List.of(7, 5, 4),
                List.of(8, 6, 2),
                List.of(7, 5, 3),
                List.of(9, 6, 4),
                List.of(10, 8, 2),
                List.of(9, 7, 3)
        );

        for (int handIndex = 0; handIndex < onePairHands.length; handIndex++) {
            final Card[] hand = onePairHands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not One Pair", bestHand instanceof OnePair);

            final OnePair onePair = (OnePair) bestHand;
            assertEquals("One Pair doesn't contain correct pair rank", pairRanks[handIndex], onePair.getPairRank());
            assertEquals("Kicker ranks weren't sorted correctly", sortedKickersRanks.get(handIndex), onePair.getSortedKickersRanks());
        }
    }

    @Test
    public void testTwoPairSanity() throws PokerHandCreatorException {
        final Card[][] twoPairHands = {
                { card("A♠"),  card("A♦"),  card("K♣"),  card("K♥"),  card("7♠") }, // Aces & Kings, kicker 7
                { card("Q♣"),  card("Q♥"),  card("J♦"),  card("J♠"),  card("5♣") }, // Queens & Jacks, kicker 5
                { card("10♠"), card("10♦"), card("9♥"),  card("9♣"),  card("4♠") }, // Tens & Nines, kicker 4
                { card("8♦"),  card("8♠"),  card("7♥"),  card("7♣"),  card("3♦") }, // Eights & Sevens, kicker 3
                { card("6♣"),  card("6♥"),  card("5♦"),  card("5♠"),  card("2♣") }, // Sixes & Fives, kicker 2
                { card("K♦"),  card("K♠"),  card("Q♠"),  card("Q♦"),  card("9♥") }, // Kings & Queens, kicker 9
                { card("J♣"),  card("J♦"),  card("10♥"), card("10♣"), card("6♠") }, // Jacks & Tens, kicker 6
                { card("9♠"),  card("9♦"),  card("8♣"),  card("8♥"),  card("4♦") }, // Nines & Eights, kicker 4
                { card("7♣"),  card("7♦"),  card("6♠"),  card("6♥"),  card("3♣") }, // Sevens & Sixes, kicker 3
                { card("5♦"),  card("5♣"),  card("4♥"),  card("4♠"),  card("A♦") }  // Fives & Fours, kicker Ace
        };

        int[] highPairRanks = {
                Card.ACE_RANK, Card.QUEEN_RANK, 10, 8, 6, Card.KING_RANK, Card.JACK_RANK, 9, 7, 5
        };

        int[] lowPairRanks = {
                Card.KING_RANK, Card.JACK_RANK, 9, 7, 5, Card.QUEEN_RANK, 10, 8, 6, 4
        };

        int[] kickerRanks = {
                7, 5, 4, 3, 2, 9, 6, 4, 3, Card.ACE_RANK
        };

        for (int handIndex = 0; handIndex < twoPairHands.length; handIndex++) {
            final Card[] hand = twoPairHands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Two Pair", bestHand instanceof TwoPair);
            TwoPair twoPair = (TwoPair) bestHand;
            assertEquals("Wrong high pair rank", highPairRanks[handIndex], twoPair.getHighPairRank());
            assertEquals("Wrong low pair rank", lowPairRanks[handIndex], twoPair.getLowPairRank());
            assertEquals("Wrong kicker rank", kickerRanks[handIndex], twoPair.getKickerRank());
        }
    }
}
