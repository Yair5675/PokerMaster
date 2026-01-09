package com.example.pokermaster.util;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinatoricsTest {
    private static final Combinatorics combinatorics = new Combinatorics();

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1",
            "1, 0, 1",
            "1, 1, 1",
            "2, 0, 1",
            "2, 1, 2",
            "2, 2, 1",
            "3, 0, 1",
            "3, 1, 3",
            "3, 2, 3",
            "3, 3, 1",
            "4, 2, 6",
            "5, 2, 10",
            "5, 3, 10",
            "6, 3, 20",
            "7, 3, 35",
            "8, 4, 70",
            "10, 5, 252",
            "45, 2, 990",
            "46, 3, 15180",
            "48, 4, 194580",
            "49, 5, 1906884",
            "50, 6, 15890700",
            "51, 7, 115775100",
            "52, 8, 752538150",
            "53, 9, 4431613550",
            "53, 10, 19499099620"
    })
    public void testNChooseR_sanity(long n, long r, long expected) {
        assertEquals(expected, combinatorics.nChooseR(n, r));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "1, 2",
            "2, 3",
            "3, 4",
            "4, 5",
            "5, 6",
            "5, 10",
            "10, 11",
            "10, 20",
            "20, 21"
    })
    public void testNChooseR_returnsZero_whenRGreaterThanN(int n, int r) {
        assertEquals(0, combinatorics.nChooseR(n, r));
    }

    @ParameterizedTest
    @CsvSource({
            "67,33",
            "68,34",
            "69,34",
            "70,35",
            "75,37"
    })
    public void testNChooseR_throwsArithmeticException_whenResultLargerThanLongMaxValue(int n, int r) {
        assertThrows(ArithmeticException.class, () -> combinatorics.nChooseR(n, r));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1",
            "1, 0, 1",
            "1, 1, 1",
            "2, 0, 1",
            "2, 1, 2",
            "2, 2, 1",
            "3, 0, 1",
            "3, 1, 3",
            "3, 2, 3",
            "3, 3, 1",
            "4, 2, 6",
            "5, 2, 10",
            "5, 3, 10",
            "6, 3, 20",
            "7, 3, 35",
            "8, 4, 70",
            "10, 5, 252"
    })
    public void testForEachCombination_sanity(int n, int r, int nCr) {
        Set<List<Integer>> combinations = new HashSet<>();

        combinatorics.forEachCombination(n, r, (combination) -> {
            assertEquals(r, combination.size());
            for (int i = 0; i < combination.size(); i++) {
                int element = combination.get(i);
                assertTrue(
                        0 <= element && element < n,
                        "Combination element out of range"
                );

                if (i > 0) {
                    assertTrue(
                            element > combination.get(i - 1),
                            "Combination doesn't contain ascending elements"
                    );
                }
            }

            assertTrue(combinations.add(List.copyOf(combination)));
        });

        assertEquals(nCr, combinations.size());
    }
}
