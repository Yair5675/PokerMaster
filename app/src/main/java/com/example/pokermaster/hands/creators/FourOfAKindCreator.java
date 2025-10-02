package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.FourOfAKind;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.creators.exceptions.InvalidRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.MissingRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.List;
import java.util.Map;


/**
 * A creator class for the {@link FourOfAKind} hand.
 */
public class FourOfAKindCreator implements PokerHandCreator {
    private static final int KICKER_REPETITIONS = 1;
    private static final int QUADRUPLET_REPETITIONS = 4;

    /**
     * Creates a {@link FourOfAKind} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A {@link FourOfAKind} instance.
     * @throws InvalidRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} contains a
     *                                         mapping to a repetitions value other than 1 or 4.
     *                                         This cannot be since in a Four-Of-A-Kind there is
     *                                         only one quadruplet and one kicker.
     * @throws MissingRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} does not contain
     *                                         a mapping to a repetitions value of either 1 or 4
     *                                         (both must have a mapping due to the aforementioned
     *                                         explanation).
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) throws PokerHandCreatorException {
        int quadrupletRank = -1;
        int kickerRank = -1;
        for (Map.Entry<Integer, Integer> rankToRepetitions : properties.getRankToRepetitions().entrySet()) {
            final int repetitions = rankToRepetitions.getValue();
            if (repetitions == KICKER_REPETITIONS)
                kickerRank = rankToRepetitions.getKey();
            else if (repetitions == QUADRUPLET_REPETITIONS)
                quadrupletRank = rankToRepetitions.getKey();
            else
                throw new InvalidRankRepetitionsException(String.format(
                        "Four-Of-A-Kind hand can only have repetitions of 1 or 4 (%d found)",
                        repetitions
                ));
        }
        if (quadrupletRank < 0)
            throw new MissingRankRepetitionsException(
                    "Didn't find quadruplet rank in Four-Of-A-Kind hand properties");
        if (kickerRank < 0)
            throw new MissingRankRepetitionsException(
                    "Didn't find kicker rank in Four-Of-A-Kind hand properties");

        return new FourOfAKind(quadrupletRank, kickerRank);
    }
}
