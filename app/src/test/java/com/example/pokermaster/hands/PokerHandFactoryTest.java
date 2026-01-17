package com.example.pokermaster.hands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


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

    private static Card[] hand(String c1, String c2, String c3, String c4, String c5) {
        return new Card[] {card(c1), card(c2), card(c3), card(c4), card(c5)};
    }

    /**
     * Checks that every card in {@code expectedHand} also exists in {@code actualHand}.
     * If a card is missing from {@code actualHand}, it is returned in the optional.
     * If all cards exist, an empty optional is returned.
     */
    private Optional<Card> findMissingCard(Card[] expectedHand, List<Card> actualHand) {
        for (Card expectedCard : expectedHand) {
            if (!actualHand.contains(expectedCard)) {
                return Optional.of(expectedCard);
            }
        }
        return Optional.empty();
    }

    private static Stream<Arguments> highCardHands() {
        return Stream.of(
                Arguments.of((Object) hand("A♠", "9♥", "7♦", "4♣", "2♠")),
                Arguments.of((Object) hand("K♦", "J♣", "9♠", "6♥", "3♦")),
                Arguments.of((Object) hand("Q♣", "10♦", "8♠", "5♥", "2♣")),
                Arguments.of((Object) hand("J♦", "9♣", "7♥", "4♠", "3♦")),
                Arguments.of((Object) hand("10♠", "8♦", "6♥", "4♣", "2♦")),
                Arguments.of((Object) hand("9♥", "7♠", "5♦", "3♣", "2♥")),
                Arguments.of((Object) hand("A♦", "Q♠", "8♥", "5♣", "3♦")),
                Arguments.of((Object) hand("K♣", "10♥", "9♦", "6♠", "4♥")),
                Arguments.of((Object) hand("Q♦", "J♠", "8♣", "6♥", "3♠")),
                Arguments.of((Object) hand("J♣", "9♦", "7♠", "5♥", "2♣"))
        );
    }

    private static Stream<Arguments> onePairHands() {
        return Stream.of(
                Arguments.of(
                        hand("A♠", "A♦", "9♥", "6♣", "3♠"), Card.ACE_RANK, List.of(9, 6, 3)
                ),
                Arguments.of(
                        hand("K♣", "K♥", "10♦", "8♠", "4♣"), Card.KING_RANK, List.of(10, 8, 4)
                ),
                Arguments.of(
                        hand("Q♦", "Q♠", "9♣", "5♥", "2♦"), Card.QUEEN_RANK, List.of(9, 5, 2)
                ),
                Arguments.of(
                        hand("J♥", "J♣", "8♦", "6♠", "3♥"), Card.JACK_RANK, List.of(8, 6, 3)
                ),
                Arguments.of(
                        hand("10♣", "10♠", "7♦", "5♥", "4♠"), 10, List.of(7, 5, 4)
                ),
                Arguments.of(
                        hand("9♦", "9♠", "8♣", "6♥", "2♦"), 9, List.of(8, 6, 2)
                ),
                Arguments.of(
                        hand("8♠", "8♦", "7♥", "5♣", "3♠"), 8, List.of(7, 5, 3)
                ),
                Arguments.of(
                        hand("7♣", "7♠", "9♦", "6♥", "4♣"), 7, List.of(9, 6, 4)
                ),
                Arguments.of(
                        hand("6♦", "6♠", "10♥", "8♣", "2♥"), 6, List.of(10, 8, 2)
                ),
                Arguments.of(
                        hand("5♣", "5♦", "9♠", "7♥", "3♣"), 5, List.of(9, 7, 3)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("highCardHands")
    public void testHighCardSanity(Card[] hand) {
        final PokerHand bestHand = PokerHandFactory.createBestHand(
                hand[0], hand[1], hand[2], hand[3], hand[4]
        );
        assertInstanceOf(HighCard.class, bestHand, "Best hand was not High Card");

        final List<Card> highCardCards = bestHand.getCards();
        final Optional<Card> missingCard = findMissingCard(hand, highCardCards);
        String missingCardStr = missingCard
                .map(Object::toString)
                .orElse("no missing card");
        assertTrue(
                missingCard.isEmpty(),
                String.format("HighCard#getCards missing card %s", missingCardStr)
        );
    }

    @ParameterizedTest
    @MethodSource("onePairHands")
    public void testOnePairSanity(Card[] hand, int pairRank, List<Integer> sortedKickersRanks)
    {
        final PokerHand bestHand = PokerHandFactory.createBestHand(
                hand[0], hand[1], hand[2], hand[3], hand[4]
        );
        assertInstanceOf(OnePair.class, bestHand, "Best hand was not One Pair");

        final OnePair onePair = (OnePair) bestHand;
        assertEquals(pairRank, onePair.getPairRank(), "One Pair doesn't contain correct pair rank");
        assertEquals(sortedKickersRanks, onePair.getSortedKickersRanks(), "Kicker ranks weren't sorted correctly");

        final List<Card> onePairCards = bestHand.getCards();
        final Optional<Card> missingCard = findMissingCard(hand, onePairCards);
        String missingCardStr = missingCard
                .map(Object::toString)
                .orElse("no missing card");
        assertTrue(
                missingCard.isEmpty(),
                String.format("OnePair#getCards missing card %s", missingCardStr)
        );
    }

    @Test
    public void testTwoPairSanity() {
        // TODO - make this parametrized when you feel like it (and the rest of the tests)
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
            assertInstanceOf(TwoPair.class, bestHand, "Best hand was not Two Pair");
            TwoPair twoPair = (TwoPair) bestHand;
            assertEquals(highPairRanks[handIndex], twoPair.getHighPairRank(), "Wrong high pair rank");
            assertEquals(lowPairRanks[handIndex], twoPair.getLowPairRank(), "Wrong low pair rank");
            assertEquals(kickerRanks[handIndex], twoPair.getKickerRank(), "Wrong kicker rank");

            final List<Card> twoPairCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, twoPairCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("TwoPair#getCards missing card %s", missingCardStr)
            );
        }
    }

    @Test
    public void testThreeOfAKindSanity() {
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
            assertInstanceOf(ThreeOfAKind.class, bestHand, "Best hand was not Three Of A Kind");
            ThreeOfAKind toakHand = (ThreeOfAKind) bestHand;
            assertEquals(highKickerRanks[handIndex], toakHand.getHighKickerRank(), "Wrong high kicker rank");
            assertEquals(lowKickerRanks[handIndex], toakHand.getLowKickerRank(), "Wrong low kicker rank");
            assertEquals(tripletRanks[handIndex], toakHand.getMatchingCardRank(), "Wrong triplet rank");

            final List<Card> threeOfAKindCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, threeOfAKindCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("ThreeOfAKind#getCards missing card %s", missingCardStr)
                        );
        }
    }

    @Test
    public void testStraightSanity() {
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
            assertInstanceOf(Straight.class, bestHand, "Best hand was not Straight");
            Straight straight = (Straight) bestHand;
            assertEquals(straightHighRanks[handIndex], straight.getHighestCardRank(), "Wrong highest card rank");

            final List<Card> straightCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, straightCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("Straight#getCards missing card %s", missingCardStr)
            );
        }
    }

    @Test
    public void testFlushSanity() {
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
            assertInstanceOf(Flush.class, bestHand, "Best hand was not Flush");
            Flush flush = (Flush) bestHand;
            assertEquals(flushSuits[handIndex], flush.getMatchingSuit(), "Wrong matching suit");

            final List<Card> flushCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, flushCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("Flush#getCards missing card %s", missingCardStr)
            );
        }
    }

    @Test
    public void testFullHouseSanity() {
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
            assertInstanceOf(FullHouse.class, bestHand, "Best hand was not Full House");
            FullHouse fullHouse = (FullHouse) bestHand;
            assertEquals(tripletRanks[handIndex], fullHouse.getTripletRank(), "Wrong triplet rank");
            assertEquals(pairRanks[handIndex], fullHouse.getPairRank(), "Wrong pair rank");

            final List<Card> fullHouseCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, fullHouseCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("FullHouse#getCards missing card %s", missingCardStr)
            );
        }
    }

    @Test
    public void testFourOfAKind() {
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
            assertInstanceOf(FourOfAKind.class, bestHand, "Best hand was not Four Of A Kind");
            FourOfAKind fourOfAKind = (FourOfAKind) bestHand;
            assertEquals(quadrupletRanks[handIndex], fourOfAKind.getMatchingCardRank(), "Wrong quadruplet rank");
            assertEquals(kickerRanks[handIndex], fourOfAKind.getKickerRank(), "Wrong kicker rank");

            final List<Card> fourOfAKindCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, fourOfAKindCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("FourOfAKind#getCards missing card %s", missingCardStr)
            );
        }
    }

    @Test
    public void testStraightFlushSanity() {
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
        };

        int[] straightFlushHighRanks = {
                5, 6, 7, 8, 9, 10, Card.JACK_RANK, Card.QUEEN_RANK, Card.KING_RANK, Card.ACE_RANK
        };

        for (int handIndex = 0; handIndex < hands.length; handIndex++) {
            final Card[] hand = hands[handIndex];
            final PokerHand bestHand = PokerHandFactory.createBestHand(
                    hand[0], hand[1], hand[2], hand[3], hand[4]
            );
            assertInstanceOf(StraightFlush.class, bestHand, "Best hand was not Straight Flush");
            StraightFlush straightFlush = (StraightFlush) bestHand;
            assertEquals(straightFlushHighRanks[handIndex], straightFlush.getHighestCardRank(), "Wrong quadruplet rank");

            final List<Card> straightFlushCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(hand, straightFlushCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("StraightFlush#getCards missing card %s", missingCardStr)
            );
        }
    }

    @Test
    public void testRoyalFlushSanity() {
        Card[][] royalFlushHands = {
                { card("10♠"), card("J♠"), card("Q♠"), card("K♠"), card("A♠") }, // Spades
                { card("10♥"), card("J♥"), card("Q♥"), card("K♥"), card("A♥") }, // Hearts
                { card("10♦"), card("J♦"), card("Q♦"), card("K♦"), card("A♦") }, // Diamonds
                { card("10♣"), card("J♣"), card("Q♣"), card("K♣"), card("A♣") }  // Clubs
        };

        for (Card[] royalFlushHand : royalFlushHands) {
            PokerHand bestHand = PokerHandFactory.createBestHand(
                    royalFlushHand[0], royalFlushHand[1], royalFlushHand[2], royalFlushHand[3],
                    royalFlushHand[4]
            );
            assertInstanceOf(RoyalFlush.class, bestHand, "Best hand was not Royal Flush");

            final List<Card> royalFlushCards = bestHand.getCards();
            final Optional<Card> missingCard = findMissingCard(royalFlushHand, royalFlushCards);
            String missingCardStr = missingCard
                    .map(Object::toString)
                    .orElse("no missing card");
            assertTrue(
                    missingCard.isEmpty(),
                    String.format("RoyalFlush#getCards missing card %s", missingCardStr)
            );
        }
    }
}
