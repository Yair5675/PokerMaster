package com.example.pokermaster.hands;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.creators.FlushCreator;
import com.example.pokermaster.hands.creators.FourOfAKindCreator;
import com.example.pokermaster.hands.creators.FullHouseCreator;
import com.example.pokermaster.hands.creators.HighCardCreator;
import com.example.pokermaster.hands.creators.OnePairCreator;
import com.example.pokermaster.hands.creators.PokerHandCreator;
import com.example.pokermaster.hands.creators.RoyalFlushCreator;
import com.example.pokermaster.hands.creators.StraightCreator;
import com.example.pokermaster.hands.creators.StraightFlushCreator;
import com.example.pokermaster.hands.creators.ThreeOfAKindCreator;
import com.example.pokermaster.hands.creators.TwoPairCreator;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A factory-like class whose purpose is to create the best {@link PokerHand hand} possible from 5
 * cards given to it.
 */
public final class PokerHandFactory {
    private static Map<Predicate<HandProperties>, PokerHandCreator> sCreatorsPredicates = null;

    /**
     * Initializes the mapping between {@link HandProperties} predicates and matching
     * implementations of {@link PokerHandCreator}.
     * <p>
     *     This method should be used for lazy initialization, and while not strictly required it
     *     should be used once.
     * </p>
     */
    private static void initializeHandCreatorsMapping() {
        // IMPORTANT - use LinkedHashMap to maintain insertion order (hands must be created from
        // best to worst)
        sCreatorsPredicates = new LinkedHashMap<>();
        sCreatorsPredicates.put(HandProperties::isRoyalFlush, new RoyalFlushCreator());
        sCreatorsPredicates.put(HandProperties::isStraightFlush, new StraightFlushCreator());
        sCreatorsPredicates.put(HandProperties::isFourOfAKind, new FourOfAKindCreator());
        sCreatorsPredicates.put(HandProperties::isFullHouse, new FullHouseCreator());
        sCreatorsPredicates.put(HandProperties::isFlush, new FlushCreator());
        sCreatorsPredicates.put(HandProperties::isStraight, new StraightCreator());
        sCreatorsPredicates.put(HandProperties::isThreeOfAKind, new ThreeOfAKindCreator());
        sCreatorsPredicates.put(HandProperties::isTwoPair, new TwoPairCreator());
        sCreatorsPredicates.put(HandProperties::isOnePair, new OnePairCreator());
        // Default mapping since everything is a high card:
        sCreatorsPredicates.put(_hp -> true, new HighCardCreator());
    }

    /**
     * Given 5 cards to make a {@link PokerHand} from, the function does just that.
     * It ensures the returned hand is the best hand that can be made from the given 5 cards.
     * @param card1 First card of the hand (order is meaningless though).
     * @param card2 Second card of the hand (order is meaningless though).
     * @param card3 Third card of the hand (order is meaningless though).
     * @param card4 Fourth card of the hand (order is meaningless though).
     * @param card5 Fifth card of the hand (order is meaningless though).
     * @return The best Poker Hand that can be made from these five cards.
     * @throws PokerHandCreatorException If any exception occurred while creating the best hand
     *                                   using the given hand.
     *                                   TODO: Consider wrapping this in a factory exception maybe?
     * @throws IllegalStateException If somehow the default predicate of {@link HighCardCreator}
     *                               didn't catch the default case and an unreachable point of code
     *                               was reached (this should NEVER be happening. Probably some
     *                               malicious reflection magic is involved).
     */
    public static PokerHand createBestHand(Card card1, Card card2, Card card3, Card card4, Card card5) throws PokerHandCreatorException {
        final List<Card> rawHand = new ArrayList<>(List.of(card1, card2, card3, card4, card5));
        rawHand.sort(Comparator.comparing(Card::getRank));

        final HandProperties handProperties = computeHandFlags(rawHand);
        if (sCreatorsPredicates == null)
            initializeHandCreatorsMapping();

        for (Map.Entry<Predicate<HandProperties>, PokerHandCreator> predicateToCreator : sCreatorsPredicates.entrySet()) {
            final Predicate<HandProperties> handPredicate = predicateToCreator.getKey();
            final PokerHandCreator creator = predicateToCreator.getValue();
            if (handPredicate.test(handProperties))
                return creator.create(rawHand, handProperties);
        }
        throw new IllegalStateException(
                "Unreachable code point reached - the default predicate of HighCardCreator wasn't reached somehow"
        );
    }

    /**
     * Given a list of card sorted by their rank ascendingly, the function returns a
     * {@link HandProperties} based on the cards.
     * @param rankSortedHand A list of cards sorted according to their rank, from lowest to highest.
     * @return A {@link HandProperties} object whose values are based on rankSortedHand.
     */
    private static HandProperties computeHandFlags(List<Card> rankSortedHand) {
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
}
