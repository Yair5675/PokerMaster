package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.OnePair;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.creators.exceptions.InvalidHandSizeException;
import com.example.pokermaster.hands.creators.exceptions.MissingRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A creator class for {@link OnePair}.
 */
public class OnePairCreator implements PokerHandCreator {
    private static final int PAIR_REPETITIONS = 2;

    /**
     * Creates a {@link OnePair} hand from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A new {@link OnePair} instance.
     * @throws MissingRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} does not have an
     *                                         entry with exactly 2 repetitions (i.e - the hand does
     *                                         not hold a pair).
     * @apiNote A hand with multiple hands is accepted here. For example, if a
     *          {@link com.example.pokermaster.hands.TwoPair TwoPair} hand is given, only one of its
     *          pairs will be chosen and the other one will be put in the kickers list of
     *          {@link OnePair}.
     *          With this in mind, the order of the selected pairs is not consistent and should not
     *          be relied upon.
     */

    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) throws PokerHandCreatorException {
        if (rawHand.size() != PokerHand.HAND_SIZE)
            throw new InvalidHandSizeException(String.format(
                    "Expected %d cards in raw hand, got %d", PokerHand.HAND_SIZE, rawHand.size()
            ));

        int pairRank = Integer.MIN_VALUE;
        final List<Integer> kickerRanks = new ArrayList<>(rawHand.size() - PAIR_REPETITIONS);
        for (Map.Entry<Integer, Integer> rankToRepetitions : properties.getRankToRepetitions().entrySet()) {
            final int rank = rankToRepetitions.getKey();
            final int repetitions = rankToRepetitions.getValue();
            if (repetitions == PAIR_REPETITIONS && pairRank < Card.MIN_RANK) {
                pairRank = rank;
                continue;
            }
            for (int repetition = 0; repetition < repetitions; repetition++)
                kickerRanks.add(rank);
        }

        if (pairRank < Card.MIN_RANK)
            throw new MissingRankRepetitionsException("Didn't find pair rank in hand");
        return new OnePair(pairRank, kickerRanks);
    }
}
