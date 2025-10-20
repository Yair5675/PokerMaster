package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A class holding specific features about a {@link PokerHand}, and offers predicates that tell
 * the user whether the hand is of a specific ranking or not ({@link HandProperties#isFlush()},
 * {@link HandProperties#isFullHouse()}, etc...).
 * <br>
 * Note that higher-ranked hands also satisfy lower-ranked ones. For example, a {@link RoyalFlush}
 * is also a {@link StraightFlush}, and a {@link FullHouse} is also a {@link ThreeOfAKind}.
 * For this reason, when using the predicates, do so from strongest hand ranking to weakest.
 */
public class HandProperties {
    /* Rank of the highest card */
    private final int mHighCardRank;

    /* Whether all 5 cards have the same suit */
    private final boolean mIsSameSuit;

    /* Whether all 5 cards are in sequential order */
    private final boolean mIsSequential;

    /* A mapping between repeating card ranks and the number they are found in the cards */
    private final Map<Integer, Integer> mRankToRepetitions;


    /**
     * Constructs a new instance of {@link HandProperties}.
     * @param highCardRank The highest rank found in the hand (from {@link Card#MIN_RANK} to
     *                     {@link Card#MAX_RANK} inclusively).
     * @param isSameSuit Whether all cards in the hand share the same
     *                   {@link com.example.pokermaster.cards.Suit Suit}.
     * @param isSequential Whether the ranks of all cards in the hand are sequential.
     * @param rankToRepetitions A mapping between the rank of a card and the number of times the
     *                          rank appears in the hand.
     *                          For example, if {@link Card#QUEEN_RANK} appears thrice, then the
     *                          mapping would contain an entry mapping {@link Card#QUEEN_RANK} to 3.
     */
    public HandProperties(
            int highCardRank, boolean isSameSuit, boolean isSequential,
            Map<Integer, Integer> rankToRepetitions
    ) {
        mHighCardRank = highCardRank;
        mIsSameSuit = isSameSuit;
        mIsSequential = isSequential;
        mRankToRepetitions = rankToRepetitions;
    }

    /**
     * Given a list of card sorted by their rank ascendingly, the function returns a
     * {@link HandProperties} instance based on the cards.
     * @param rankSortedHand A list of cards sorted according to their rank, from lowest to highest.
     * @return A {@link HandProperties} object whose values are based on rankSortedHand.
     * @apiNote It is the responsibility OF THE CALLER to ensure the list is sorted.
     */
    public static HandProperties computePropertiesFromSortedRawHand(List<Card> rankSortedHand) {
        final Map<Integer, Integer> rankToRepetitions = new HashMap<>();
        boolean isSameSuit = true;
        boolean isSequential = true;

        final int firstRank = rankSortedHand.get(0).getRank();
        rankToRepetitions.put(firstRank, 1);

        final boolean isLowAceStraightPossible = (
                rankSortedHand.get(3).getRank() == 5 &&
                rankSortedHand.get(4).getRank() == Card.ACE_RANK
        );

        int highCardRank = firstRank;

        for (int i = 1; i < rankSortedHand.size(); i++) {
            final Card currentCard = rankSortedHand.get(i);
            final Card previousCard = rankSortedHand.get(i - 1);

            highCardRank = Math.max(highCardRank, currentCard.getRank());
            isSameSuit &= currentCard.getSuit() == previousCard.getSuit();
            isSequential &= (
                    currentCard.getRank() == previousCard.getRank() + 1 ||
                    (previousCard.getRank() == 5 && isLowAceStraightPossible)
            );
            rankToRepetitions.put(
                    currentCard.getRank(),
                    rankToRepetitions.getOrDefault(currentCard.getRank(), 0) + 1
            );
        }

        // In the case of a low-ace straight, the ace's rank should be considered '1':
        if (isSequential && isLowAceStraightPossible)
            highCardRank = 5;

        return new HandProperties(highCardRank, isSameSuit, isSequential, rankToRepetitions);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof HandProperties)) return false;
        final HandProperties handProperties = (HandProperties) other;

        final boolean sameHighRank = mHighCardRank == handProperties.mHighCardRank;
        final boolean sameSuitEquals = mIsSameSuit == handProperties.mIsSameSuit;
        final boolean sequentialEquals = mIsSequential == handProperties.mIsSequential;
        final boolean rankToRepetitionsEquals = mRankToRepetitions.equals(handProperties.mRankToRepetitions);

        return sameHighRank && sameSuitEquals && sequentialEquals && rankToRepetitionsEquals;
    }

    public int getHighCardRank() {
        return mHighCardRank;
    }

    public boolean isSameSuit() {
        return mIsSameSuit;
    }

    public boolean isSequential() {
        return mIsSequential;
    }

    public Map<Integer, Integer> getRankToRepetitions() {
        return mRankToRepetitions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mHighCardRank, mIsSameSuit, mIsSequential, mRankToRepetitions);
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link RoyalFlush} based on its properties.
     * @return {@code true} if the hand is a Royal Flush.
     */
    public boolean isRoyalFlush() {
        return mIsSameSuit && mIsSequential && mHighCardRank == Card.ACE_RANK;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link StraightFlush} based on its properties.
     * @return {@code true} if the hand is a Straight Flush.
     * @apiNote A {@link RoyalFlush} is technically a {@link StraightFlush}, and will cause the
     *          method to return {@code true}. For this reason, when querying the type of hand, do
     *          it from the best to worst hand.
     */
    public boolean isStraightFlush() {
        return mIsSameSuit && mIsSequential;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link FourOfAKind} based on its properties.
     * @return {@code true} if the hand is a Four-Of-A-Kind.
     */
    public boolean isFourOfAKind() {
        for (int repetitions : mRankToRepetitions.values()) {
            if (repetitions >= 4)
                return true;
        }
        return false;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link FullHouse} based on its properties.
     * @return {@code true} if the hand is a Full-House.
     */
    public boolean isFullHouse() {
        boolean isTripletFound = false;
        boolean isPairFound = false;
        for (int repetitions : mRankToRepetitions.values()) {
            isTripletFound |= repetitions == 3;
            isPairFound |= repetitions == 2;
        }
        return isTripletFound && isPairFound;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link Flush} based on its properties.
     * @return {@code true} if the hand is a Flush.
     * @apiNote A {@link RoyalFlush} and {@link StraightFlush} are technically a {@link Flush},
     *          and will cause the method to return {@code true}. For this reason, when querying the
     *          type of hand, do it from the best to worst hand.
     */
    public boolean isFlush() {
        return mIsSameSuit;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link Straight} based on its properties.
     * @return {@code true} if the hand is a Straight.
     * @apiNote A {@link RoyalFlush} and {@link StraightFlush} are technically a
     *          {@link Straight}, and will cause the method to return {@code true}. For this
     *          reason, when querying the type of hand, do it from the best to worst hand.
     */
    public boolean isStraight() {
        return mIsSequential;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link ThreeOfAKind} based on its properties.
     * @return {@code true} if the hand is a Three-Of-A-Kind.
     * @apiNote A {@link FullHouse} contains a {@link ThreeOfAKind}, and a {@link FourOfAKind}
     *          also contains three cards, therefore both will cause the method to return
     *          {@code true}. For this reason, when querying the type of hand, do it from the
     *          best to worst hand.
     */
    public boolean isThreeOfAKind() {
        for (int repetitions : mRankToRepetitions.values()) {
            if (repetitions >= 3)
                return true;
        }
        return false;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link TwoPair} based on its properties.
     * @return {@code true} if the hand is a Two-Pair.
     */
    public boolean isTwoPair() {
        int pairsFound = 0;
        for (int repetitions : mRankToRepetitions.values()) {
            if (repetitions == 2)
                pairsFound++;
        }
        return pairsFound == 2;
    }

    /**
     * Checks if the {@link PokerHand Hand} is a {@link OnePair} based on its properties.
     * @return {@code true} if the hand is a One-Pair.
     * @apiNote Both {@link FullHouse} and {@link TwoPair} contain at least one pair of matching
     *          ranks, which qualifies them as a {@link OnePair} and therefore would make this
     *          function return {@code true}.
     *          For this reason, when querying the type of hand, do it from the best to worst
     *          hand.
     */
    public boolean isOnePair() {
        return mRankToRepetitions
                .values()
                .stream()
                .anyMatch(repetitions -> repetitions == 2);
    }
}