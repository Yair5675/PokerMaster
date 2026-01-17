package com.example.pokermaster.cards.indexers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.pokermaster.annotations.ExpensiveTest;
import com.example.pokermaster.cards.Card;
import com.example.pokermaster.cards.Suit;
import com.example.pokermaster.util.Combinatorics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CardCombinationIndexerTest {
    private static final int BUCKET_SIZE = 10_000;
    private static final Combinatorics combinatorics = new Combinatorics();
    private static final CardIndexer cardIndexer = new CardIndexer();
    private static final CardCombinationIndexer combinationIndexer = new CardCombinationIndexer();
    private static final int POSSIBLE_CARD_VALUES = (
            (Card.MAX_RANK - Card.MIN_RANK + 1) * Suit.values().length
    );
    private static final int SHOWDOWN_HAND_SIZE = 7;

    @Test
    @ExpensiveTest
    public void testGetCombinationIndex_Sanity() {
        int maxExpectedIndex = Math.toIntExact(combinatorics.nChooseR(POSSIBLE_CARD_VALUES, SHOWDOWN_HAND_SIZE) - 1);
        List<Card> combination = new ArrayList<>(SHOWDOWN_HAND_SIZE);
        // Creating an array of maxExpectedIndex + 1 or using a HashSet cause OutOfMemoryException,
        // so we have to divide and conquer
        int[] seenBuckets = new int[(maxExpectedIndex / BUCKET_SIZE) + 1];

        for (int i = 0; i < SHOWDOWN_HAND_SIZE; i++)
            combination.add(new Card(Card.MIN_RANK, Suit.DIAMOND));

        combinatorics.forEachCombination(POSSIBLE_CARD_VALUES, SHOWDOWN_HAND_SIZE, (combinationIndices) -> {
            for (int i = 0; i < SHOWDOWN_HAND_SIZE; i++) {
                cardIndexer.setCardToIndex(combination.get(i), combinationIndices.get(i));
            }
            assertEquals(SHOWDOWN_HAND_SIZE, combination.size());
            int index = Math.toIntExact(combinationIndexer.getCombinationIndex(combination));

            assertTrue(0 <= index && index <= maxExpectedIndex);
            seenBuckets[index / BUCKET_SIZE]++;
            assertTrue(seenBuckets[index / BUCKET_SIZE] <= BUCKET_SIZE);
        });

        long processedIndices = 0;
        for (int i = 0; i < seenBuckets.length; i++) {
            int bucket = seenBuckets[i];
            processedIndices += bucket;

            // This check ensures no index is repeated for two distinct combinations - since if it
            // would then some bucket later will be missing 1 index counted for it:
            int expectedBucketCount = (
                    i < seenBuckets.length - 1 ? BUCKET_SIZE : (maxExpectedIndex % BUCKET_SIZE) + 1
            );
            assertEquals(expectedBucketCount, bucket);
        }
        assertEquals(maxExpectedIndex + 1, processedIndices);
    }
}
