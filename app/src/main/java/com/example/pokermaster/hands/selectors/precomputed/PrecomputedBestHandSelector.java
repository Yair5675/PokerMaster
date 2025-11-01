package com.example.pokermaster.hands.selectors.precomputed;

import com.example.pokermaster.hands.selectors.BestHandSelector;

import java.io.IOException;

/**
 * Special kind of {@link BestHandSelector} that precomputes the best hands for optimization
 * purposes.
 */
public interface PrecomputedBestHandSelector extends BestHandSelector {
    /**
     * Precomputes the solutions to the best hand selections. Each precomputed-best-hand-selector
     * should set up any information necessary here.
     * @throws IOException If storing the precomputed solutions resulted in an IO error.
     */
    void precomputeBestHands() throws IOException;
}
