package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.TwoPair;
import com.example.pokermaster.hands.creators.exceptions.InvalidRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.MissingRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.List;
import java.util.Map;

/**
 * A creator class for the {@link TwoPair} hand.
 */
public class TwoPairCreator implements PokerHandCreator {
    private static final int PAIRS_COUNT = 2;
    private static final int PAIRS_REPETITIONS = 2;
    private static final int KICKER_REPETITIONS = 1;

    /**
     * Creates a {@link TwoPair} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A new {@link TwoPair} instance.
     * @throws InvalidRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} contains a
     *                                         mapping to a repetitions value other than 2 or 1.
     *                                         This cannot be since in a Two-Pair there are 2 pairs
     *                                         (which means two mappings to 2 repetitions) and a
     *                                         single kicker (which means one mapping to 1
     *                                         repetition).
     * @throws MissingRankRepetitionsException If less than two pairs were found or a kicker wasn't
     *                                         found at all.
     * @apiNote Two-Pair requires two pairs of cards with DIFFERENT ranks, so 4 repetitions of the
     *          same card would result in an exception.
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) throws PokerHandCreatorException {
        final int[] pairs = new int[PAIRS_COUNT];
        int pairsIndex = 0;
        int kickerRank = -1;

        for (Map.Entry<Integer, Integer> rankToRepetitions : properties.getRankToRepetitions().entrySet()) {
            final int rank = rankToRepetitions.getKey(), repetitions = rankToRepetitions.getValue();
            if (repetitions == PAIRS_REPETITIONS)
                pairs[pairsIndex++] = rank;
            else if (repetitions == KICKER_REPETITIONS)
                kickerRank = rank;
            else
                throw new InvalidRankRepetitionsException(String.format(
                        "Two-Pair hand can only have repetitions of %d or %d (found %d)",
                        PAIRS_REPETITIONS, KICKER_REPETITIONS, repetitions
                ));
        }

        if (pairsIndex < PAIRS_COUNT)
            throw new MissingRankRepetitionsException(String.format(
                    "Expected %d pairs, found %d", PAIRS_COUNT, pairsIndex
            ));
        if (kickerRank < 0)
            throw new MissingRankRepetitionsException("Didn't find kicker rank");
        return new TwoPair(pairs[0], pairs[1], kickerRank, rawHand);
    }
}
