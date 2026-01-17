package com.example.pokermaster.hands.selectors.precomputed;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.hands.PokerHand;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface BestHandSolutionWriter {
    /**
     * Given the card combination at showdown, and the best hand computed from said combination,
     * the function writes the solution to the given output stream.
     * @param showdownCombination The card combination shown at showdown.
     * @param bestHand The best hand possible out of the cards from the showdown.
     * @param solutionOutputStream An output stream to which the best hand solution will be written
     *                             to.
     * @throws IOException If writing to the output stream fails.
     */
    void writeBestHandSolution(
            List<Card> showdownCombination, PokerHand bestHand, OutputStream solutionOutputStream
    ) throws IOException;
}
