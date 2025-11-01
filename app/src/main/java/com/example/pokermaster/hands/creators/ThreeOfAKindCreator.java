package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.ThreeOfAKind;
import com.example.pokermaster.hands.creators.exceptions.MissingRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.List;
import java.util.Map;

/**
 * A creator class for the {@link ThreeOfAKind} hand.
 */
public class ThreeOfAKindCreator implements PokerHandCreator {
    private static final int KICKERS_COUNT = 2;
    private static final int TRIPLET_REPETITIONS = 3;

    /**
     * Creates a {@link ThreeOfAKind} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A new {@link ThreeOfAKind} instance.
     * @throws MissingRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} doesn't contain a
     *                                         mapping with at least 3 repetitions (which means the
     *                                         rank of the triplet does not exist), or if the
     *                                         remainder of the repetitions isn't 2 (which means
     *                                         there are less than 2 kickers).
     * @apiNote Any hand with AT LEAST 3 repetitions is considered a {@link ThreeOfAKind}, which
     *          includes {@link com.example.pokermaster.hands.FourOfAKind FourOfAKind} and
     *          {@link com.example.pokermaster.hands.FullHouse FullHouse}. For this reason, the
     *          mappings returned by {@link HandProperties#getRankToRepetitions()
     *          properties.getRankToRepetitions()} CAN contain a single rank mapped to more than 3
     *          repetitions. The function is smart enough to put the rest of the repetitions as
     *          kickers.
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) throws PokerHandCreatorException {
        final int[] kickers = new int[KICKERS_COUNT];
        int kickerIndex = 0;
        int tripletRank = -1;

        for (Map.Entry<Integer, Integer> rankToRepetitions : properties.getRankToRepetitions().entrySet()) {
            int rank = rankToRepetitions.getKey(), repetitions = rankToRepetitions.getValue();
            if (repetitions >= TRIPLET_REPETITIONS) {
                tripletRank = rank;
                repetitions -= TRIPLET_REPETITIONS;
            }

            // Use for loop in case there are more than 3 repetitions and the remainder should be
            // considered kickers
            while (kickerIndex < kickers.length && repetitions > 0) {
                kickers[kickerIndex++] = rank;
                repetitions--;
            }
        }

        if (kickerIndex < KICKERS_COUNT)
            throw new MissingRankRepetitionsException(String.format(
                    "Expected %d kicker ranks, found %d", KICKERS_COUNT, kickerIndex
            ));
        if (tripletRank < 0)
            throw new MissingRankRepetitionsException("Didn't find triplet ranking");
        return new ThreeOfAKind(tripletRank, kickers[0], kickers[1]);
    }
}
