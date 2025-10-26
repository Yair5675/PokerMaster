package com.example.pokermaster.hands.selectors.precomputed;

import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.HoleCards;
import com.example.pokermaster.hands.PokerHand;
import com.example.pokermaster.hands.selectors.BestHandSelector;
import com.example.pokermaster.hands.selectors.exceptions.BestHandSelectorException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Special kind of {@link BestHandSelector} that precomputes the best hands for optimization
 * purposes.
 */
public class PrecomputedBestHandSelector implements BestHandSelector {
    private final File mPrecomputedHandsFile;

    public PrecomputedBestHandSelector(File precomputedHandsFile) {
        mPrecomputedHandsFile = precomputedHandsFile;
    }

    /**
     * Precomputes the best hands according to the given {@link BestHandSelector}, saving them in
     * the precomputed-hands file for future use.
     * @param bestHandSelector Another {@link BestHandSelector} which will be used to calculate the
     *                         best hands for every hand combination. The result of this selector
     *                         will be saved in the file and used by the current
     *                         {@link PrecomputedBestHandSelector} next time ITS {@code getBestHand}
     *                         method is called.
     * @throws IOException If writing to the
     */
    public void precomputeBestHands(BestHandSelector bestHandSelector) throws IOException {
        // TODO Complete the implementation
    }

    @Override
    public PokerHand getBestHand(HoleCards holeCards, List<Card> communityCards) throws BestHandSelectorException {
        // TODO Complete the implementation
        return null;
    }
}
