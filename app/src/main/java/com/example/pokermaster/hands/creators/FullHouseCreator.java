package com.example.pokermaster.hands.creators;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.FullHouse;
import com.example.pokermaster.hands.HandProperties;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.creators.exceptions.InvalidRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.MissingRankRepetitionsException;
import com.example.pokermaster.hands.creators.exceptions.PokerHandCreatorException;

import java.util.List;
import java.util.Map;

/**
 * Creator class for the {@link FullHouse} hand.
 */
public class FullHouseCreator implements PokerHandCreator {
    /**
     * Creates a {@link FullHouse} instance from the given raw hand and hand properties.
     * @param rawHand A list of {@link Card cards} that will make up the hand.
     * @param properties The properties of the given raw hand.
     * @return A {@link FullHouse} instance.
     * @throws InvalidRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} contains a
     *                                         mapping to a repetitions value other than 2 or 3.
     *                                         This cannot be since in a Full House there is only
     *                                         one triplet and one pair.
     * @throws MissingRankRepetitionsException If {@link HandProperties#getRankToRepetitions()
     *                                         properties.getRankToRepetitions()} does not contain
     *                                         a mapping to a repetitions value of either 2 or 3
     *                                         (both must have a mapping due to the aforementioned
     *                                         explanation).
     */
    @Override
    public PokerHand create(List<Card> rawHand, HandProperties properties) throws PokerHandCreatorException {
        int tripletRank = -1;
        int pairRank = -1;
        for (Map.Entry<Integer, Integer> rankToRepetitions : properties.getRankToRepetitions().entrySet()) {
            final int repetitions = rankToRepetitions.getValue();
            if (repetitions == 2)
                pairRank = rankToRepetitions.getKey();
            else if (repetitions == 3)
                tripletRank = rankToRepetitions.getKey();
            else
                throw new InvalidRankRepetitionsException(String.format(
                        "Full House can only have repetitions of 2 or 3 (%d found)", repetitions
                ));
        }
        if (tripletRank < 0)
            throw new MissingRankRepetitionsException("Didn't find triplet rank in hand properties");
        if (pairRank < 0)
            throw new MissingRankRepetitionsException("Didn't find pair rank in hand properties");

        return new FullHouse(tripletRank, pairRank, rawHand);
    }
}
