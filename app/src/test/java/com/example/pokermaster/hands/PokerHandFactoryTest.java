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

    @Test
    public void testThreeOfAKindSanity() throws PokerHandCreatorException {
        Card[][] threeOfAKindHands = {
                { card("A♠"), card("A♦"), card("A♣"), card("K♠"), card("7♦") }, // Triplet Aces, kickers K,7
                { card("K♥"), card("K♦"), card("K♣"), card("Q♠"), card("9♣") }, // Triplet Kings, kickers Q,9
                { card("Q♠"), card("Q♦"), card("Q♣"), card("J♣"), card("8♦") }, // Triplet Queens, kickers J,8
                { card("J♥"), card("J♦"), card("J♠"), card("10♣"), card("6♠") }, // Triplet Jacks, kickers 10,6
                { card("10♦"), card("10♠"), card("10♥"), card("9♣"), card("4♠") }, // Triplet Tens, kickers 9,4
                { card("9♠"), card("9♦"), card("9♥"), card("8♣"), card("5♦") }, // Triplet Nines, kickers 8,5
                { card("8♦"), card("8♠"), card("8♣"), card("7♥"), card("3♣") }, // Triplet Eights, kickers 7,3
                { card("7♣"), card("7♦"), card("7♠"), card("6♥"), card("2♠") }, // Triplet Sevens, kickers 6,2
                { card("6♠"), card("6♦"), card("6♣"), card("5♥"), card("3♦") }, // Triplet Sixes, kickers 5,3
                { card("5♣"), card("5♦"), card("5♥"), card("4♠"), card("2♥") }  // Triplet Fives, kickers 4,2
        };

        int[] tripletRanks = {
                Card.ACE_RANK, Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10, 9, 8, 7, 6, 5
        };

        int[] highKickerRanks = {
                Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10, 9, 8, 7, 6, 5, 4
        };

        int[] lowKickerRanks = {
                7, 9, 8, 6, 4, 5, 3, 2, 3, 2
        };

        for (int handIndex = 0; handIndex < threeOfAKindHands.length; handIndex++) {
            final Card[] hand = threeOfAKindHands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Three Of A Kind", bestHand instanceof ThreeOfAKind);
            ThreeOfAKind toakHand = (ThreeOfAKind) bestHand;
            assertEquals("Wrong high kicker rank", highKickerRanks[handIndex], toakHand.getHighKickerRank());
            assertEquals("Wrong low kicker rank", lowKickerRanks[handIndex], toakHand.getLowKickerRank());
            assertEquals("Wrong triplet rank", tripletRanks[handIndex], toakHand.getMatchingCardRank());
        }
    }

    @Test
    public void testStraightSanity() throws PokerHandCreatorException {
        Card[][] straightHands = {
                { card("A♠"), card("2♦"), card("3♣"), card("4♥"), card("5♠") },  // 5-high straight
                { card("2♣"), card("3♠"), card("4♦"), card("5♣"), card("6♥") },  // 6-high straight
                { card("3♥"), card("4♠"), card("5♦"), card("6♣"), card("7♠") },  // 7-high straight
                { card("4♣"), card("5♠"), card("6♥"), card("7♦"), card("8♣") },  // 8-high straight
                { card("5♦"), card("6♣"), card("7♠"), card("8♥"), card("9♣") },  // 9-high straight
                { card("6♠"), card("7♦"), card("8♣"), card("9♥"), card("10♠") }, // 10-high straight
                { card("7♣"), card("8♠"), card("9♦"), card("10♥"), card("J♠") }, // Jack-high straight
                { card("8♦"), card("9♣"), card("10♠"), card("J♥"), card("Q♣") }, // Queen-high straight
                { card("9♠"), card("10♦"), card("J♣"), card("Q♥"), card("K♠") }, // King-high straight
                { card("10♣"), card("J♦"), card("Q♠"), card("K♥"), card("A♦") }  // Ace-high straight
        };

        int[] straightHighRanks = {
                5, 6, 7, 8, 9, 10, Card.JACK_RANK, Card.QUEEN_RANK, Card.KING_RANK, Card.ACE_RANK
        };

        for (int handIndex = 0; handIndex < straightHands.length; handIndex++) {
            final Card[] hand = straightHands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Straight", bestHand instanceof Straight);
            Straight straight = (Straight) bestHand;
            assertEquals("Wrong highest card rank", straightHighRanks[handIndex], straight.getHighestCardRank());
        }
    }

    @Test
    public void testFlushSanity() throws PokerHandCreatorException {
        Card[][] hands = {
                { card("A♠"), card("10♠"), card("8♠"), card("5♠"), card("2♠") },
                { card("K♥"), card("J♥"), card("9♥"), card("6♥"), card("3♥") },
                { card("Q♦"), card("10♦"), card("7♦"), card("5♦"), card("2♦") },
                { card("J♣"), card("9♣"), card("6♣"), card("4♣"), card("3♣") },
                { card("10♠"), card("8♠"), card("7♠"), card("5♠"), card("4♠") },
                { card("9♥"), card("7♥"), card("6♥"), card("3♥"), card("2♥") },
                { card("8♦"), card("6♦"), card("5♦"), card("4♦"), card("3♦") },
                { card("7♣"), card("5♣"), card("4♣"), card("3♣"), card("2♣") },
                { card("A♥"), card("K♥"), card("10♥"), card("7♥"), card("6♥") },
                { card("Q♠"), card("J♠"), card("9♠"), card("8♠"), card("3♠") }
        };

        Suit[] flushSuits = {
                Suit.SPADE, Suit.HEART, Suit.DIAMOND, Suit.CLUB, Suit.SPADE,
                Suit.HEART, Suit.DIAMOND, Suit.CLUB, Suit.HEART, Suit.SPADE
        };

        for (int handIndex = 0; handIndex < hands.length; handIndex++) {
            final Card[] hand = hands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Flush", bestHand instanceof Flush);
            Flush flush = (Flush) bestHand;
            assertEquals("Wrong matching suit", flushSuits[handIndex], flush.getMatchingSuit());
        }
    }

    @Test
    public void testFullHouseSanity() throws PokerHandCreatorException {
        Card[][] fullHouseHands = {
                { card("A♠"), card("A♦"), card("A♣"), card("K♠"), card("K♦") }, // Aces full of Kings
                { card("K♣"), card("K♥"), card("K♦"), card("Q♠"), card("Q♥") }, // Kings full of Queens
                { card("Q♠"), card("Q♦"), card("Q♣"), card("J♣"), card("J♦") }, // Queens full of Jacks
                { card("J♥"), card("J♦"), card("J♠"), card("10♣"), card("10♦") }, // Jacks full of Tens
                { card("10♠"), card("10♦"), card("10♥"), card("9♣"), card("9♠") }, // Tens full of Nines
                { card("9♣"), card("9♦"), card("9♥"), card("8♠"), card("8♦") }, // Nines full of Eights
                { card("8♦"), card("8♣"), card("8♠"), card("7♥"), card("7♠") }, // Eights full of Sevens
                { card("7♣"), card("7♦"), card("7♠"), card("6♥"), card("6♠") }, // Sevens full of Sixes
                { card("6♠"), card("6♦"), card("6♣"), card("5♥"), card("5♠") }, // Sixes full of Fives
                { card("5♣"), card("5♦"), card("5♥"), card("4♠"), card("4♦") }  // Fives full of Fours
        };

        int[] tripletRanks = {
                Card.ACE_RANK, Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10, 9, 8, 7, 6, 5
        };

        int[] pairRanks = {
                Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10, 9, 8, 7, 6, 5, 4
        };

        for (int handIndex = 0; handIndex < fullHouseHands.length; handIndex++) {
            final Card[] hand = fullHouseHands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Full House", bestHand instanceof FullHouse);
            FullHouse fullHouse = (FullHouse) bestHand;
            assertEquals("Wrong triplet rank", tripletRanks[handIndex], fullHouse.getTripletRank());
            assertEquals("Wrong pair rank", pairRanks[handIndex], fullHouse.getPairRank());
        }
    }

    @Test
    public void testFourOfAKind() throws PokerHandCreatorException {
        Card[][] hands = {
                { card("A♠"), card("A♦"), card("A♣"), card("A♥"), card("K♠") }, // Quad Aces, kicker K
                { card("K♣"), card("K♥"), card("K♦"), card("K♠"), card("Q♠") }, // Quad Kings, kicker Q
                { card("Q♠"), card("Q♦"), card("Q♣"), card("Q♥"), card("J♣") }, // Quad Queens, kicker J
                { card("J♥"), card("J♦"), card("J♠"), card("J♣"), card("10♠") }, // Quad Jacks, kicker 10
                { card("10♣"), card("10♦"), card("10♥"), card("10♠"), card("9♣") }, // Quad Tens, kicker 9
                { card("9♠"), card("9♦"), card("9♣"), card("9♥"), card("8♣") }, // Quad Nines, kicker 8
                { card("8♦"), card("8♣"), card("8♠"), card("8♥"), card("7♣") }, // Quad Eights, kicker 7
                { card("7♣"), card("7♦"), card("7♠"), card("7♥"), card("6♠") }, // Quad Sevens, kicker 6
                { card("6♠"), card("6♦"), card("6♣"), card("6♥"), card("5♣") }, // Quad Sixes, kicker 5
                { card("5♣"), card("5♦"), card("5♥"), card("5♠"), card("4♦") }  // Quad Fives, kicker 4
        };

        int[] quadrupletRanks = {
                Card.ACE_RANK, Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10,
                9, 8, 7, 6, 5
        };

        int[] kickerRanks = {
                Card.KING_RANK, Card.QUEEN_RANK, Card.JACK_RANK, 10, 9,
                8, 7, 6, 5, 4
        };

        for (int handIndex = 0; handIndex < hands.length; handIndex++) {
            final Card[] hand = hands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Four Of A Kind", bestHand instanceof FourOfAKind);
            FourOfAKind fourOfAKind = (FourOfAKind) bestHand;
            assertEquals("Wrong quadruplet rank", quadrupletRanks[handIndex], fourOfAKind.getMatchingCardRank());
            assertEquals("Wrong kicker rank", kickerRanks[handIndex], fourOfAKind.getKickerRank());
        }
    }

    @Test
    public void testStraightFlushSanity() throws PokerHandCreatorException {
        Card[][] hands = {
                { card("A♠"), card("2♠"), card("3♠"), card("4♠"), card("5♠") },  // 5-high straight flush
                { card("2♥"), card("3♥"), card("4♥"), card("5♥"), card("6♥") },  // 6-high straight flush
                { card("3♦"), card("4♦"), card("5♦"), card("6♦"), card("7♦") },  // 7-high straight flush
                { card("4♣"), card("5♣"), card("6♣"), card("7♣"), card("8♣") },  // 8-high straight flush
                { card("5♠"), card("6♠"), card("7♠"), card("8♠"), card("9♠") },  // 9-high straight flush
                { card("6♥"), card("7♥"), card("8♥"), card("9♥"), card("10♥") }, // 10-high straight flush
                { card("7♦"), card("8♦"), card("9♦"), card("10♦"), card("J♦") }, // Jack-high straight flush
                { card("8♣"), card("9♣"), card("10♣"), card("J♣"), card("Q♣") }, // Queen-high straight flush
                { card("9♠"), card("10♠"), card("J♠"), card("Q♠"), card("K♠") }, // King-high straight flush
                { card("10♥"), card("J♥"), card("Q♥"), card("K♥"), card("A♥") }  // Ace-high straight flush
        };

        int[] straightFlushHighRanks = {
                5, 6, 7, 8, 9, 10, Card.JACK_RANK, Card.QUEEN_RANK, Card.KING_RANK, Card.ACE_RANK
        };

        for (int handIndex = 0; handIndex < hands.length; handIndex++) {
            final Card[] hand = hands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertTrue("Best hand was not Straight Flush", bestHand instanceof StraightFlush);
            StraightFlush straightFlush = (StraightFlush) bestHand;
            assertEquals("Wrong quadruplet rank", straightFlushHighRanks[handIndex], straightFlush.getHighestCardRank());
        }
    }
}
