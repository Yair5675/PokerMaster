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
     * @throws AssertionError If an internal invariant is violated.
     *                        This can occur in two cases:
     *                        <ul>
     *                          <li>If the default predicate of {@link HighCardCreator} somehow
     *                              fails to catch the fallback case (which should be impossible
     *                              without reflection or logic corruption).</li>
     *                          <li>If a {@link PokerHandCreatorException} is unexpectedly thrown by
     *                              a mapped creator despite its predicate matching — indicating a
     *                              developer-side logic error.</li>
     *                        </ul>
     */
    public static PokerHand createBestHand(Card card1, Card card2, Card card3, Card card4, Card card5) {
        final List<Card> rawHand = new ArrayList<>(List.of(card1, card2, card3, card4, card5));
        rawHand.sort(Comparator.comparing(Card::getRank));

        final HandProperties handProperties = HandProperties.computePropertiesFromSortedRawHand(rawHand);
        if (sCreatorsPredicates == null)
            initializeHandCreatorsMapping();

        try {
            for (Map.Entry<Predicate<HandProperties>, PokerHandCreator> predicateToCreator : sCreatorsPredicates.entrySet()) {
                final Predicate<HandProperties> handPredicate = predicateToCreator.getKey();
                final PokerHandCreator creator = predicateToCreator.getValue();
                if (handPredicate.test(handProperties))
                    return creator.create(rawHand, handProperties);
            }
        } catch (PokerHandCreatorException creationException) {
            throw new AssertionError(
                    "Hand creation predicate mapped to a creator that threw an exception",
                    creationException
            );
        }
        throw new AssertionError(
                "Unreachable code point reached - the default predicate of HighCardCreator wasn't reached somehow"
        );
    }

}
