package com.example.pokermaster.cards.indexers;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.exceptions.CombinationIndexOverflowException;
import com.example.pokermaster.util.Combinatorics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CardCombinationIndexer {
    private final Combinatorics mCombinatorics;
    private final CardIndexer mCardIndexer;

    public CardCombinationIndexer() {
        mCombinatorics = new Combinatorics();
        mCardIndexer = new CardIndexer();
    }

    /**
     * Computes a unique index larger than or equal to 0 for the given cards in a deterministic
     * manner.
     * @param combination A collection of cards which will be turned into a single number.
     * @return A number uniquely representing the current combination.
     * @throws CombinationIndexOverflowException If the combination is too large, and the index
     *                                           required to represent it uniquely is larger than
     *                                           {@link Long#MAX_VALUE}.
     */
    public long getCombinationIndex(List<Card> combination) {
        List<Integer> sortedCardIndices = combination
                .stream()
                .map(mCardIndexer::getCardIndex)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return getCombinationIndexFromDescendingCardIndices(sortedCardIndices);
    }

    /**
     * Computes a unique index larger than or equal to 0 for the given cards in a deterministic
     * manner.
     * @param combination A collection of cards which will be turned into a single number.
     * @return A number uniquely representing the current combination.
     * @throws CombinationIndexOverflowException If the combination is too large, and the index
     *                                           required to represent it uniquely is larger than
     *                                           {@link Long#MAX_VALUE}.
     */
    public long getCombinationIndex(Card ... combination) {
        List<Integer> sortedCardIndices = Arrays.stream(combination)
                .map(mCardIndexer::getCardIndex)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return getCombinationIndexFromDescendingCardIndices(sortedCardIndices);
    }

    private long getCombinationIndexFromDescendingCardIndices(List<Integer> descendingCardIndices)
            throws CombinationIndexOverflowException {
        long rank = 0, prevRank;
        final int combinationSize = descendingCardIndices.size();
        for (int i = 0; i < combinationSize; i++) {
            prevRank = rank;
            rank += mCombinatorics.nChooseR(descendingCardIndices.get(i), combinationSize - i);

            // Overflow!
            if (prevRank > rank) {
                throw new CombinationIndexOverflowException();
            }
        }

        return rank;
    }
}
