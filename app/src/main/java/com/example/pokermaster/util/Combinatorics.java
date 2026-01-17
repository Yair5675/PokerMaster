package com.example.pokermaster.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Combinatorics {
    public long nChooseR(long totalObjectsCount, long objectsToChoose) {
        if (objectsToChoose > totalObjectsCount)
            return 0;
        if (totalObjectsCount == objectsToChoose || objectsToChoose == 0)
            return 1;

        double result = 1;
        final long diff = totalObjectsCount - objectsToChoose;

        for (long i = 2; i <= totalObjectsCount; i++) {
            boolean withinDiff = i <= diff, withinObjectsToChoose = i <= objectsToChoose;
            if (withinDiff && withinObjectsToChoose)
                result /= i;
            else if (!withinDiff && !withinObjectsToChoose)
                result *= i;
        }

        if (result > Long.MAX_VALUE) {
            throw new ArithmeticException(String.format(
                    "Result of nCr(%d, %d) too large to be stored in a long",
                    totalObjectsCount, objectsToChoose
            ));
        }

        return Math.round(result);
    }

    public void forEachCombination(
            int totalObjectsCount,
            int objectsToChoose,
            Consumer<List<Integer>> combinationCallback
    ) {
        List<Integer> combination = new ArrayList<>(objectsToChoose);
        for (int i = 0; i < objectsToChoose; i++)
            combination.add(i);

        boolean moreCombinationsExist;
        do {
            combinationCallback.accept(combination);
            moreCombinationsExist = incrementCombination(totalObjectsCount, combination);
        } while (moreCombinationsExist);
    }

    private static boolean incrementCombination(int totalObjectsCount, List<Integer> combination) {
        int indexToIncrement = getRightmostIndexToIncrement(totalObjectsCount, combination);
        if (indexToIncrement == -1) {
            return false;
        }

        combination.set(indexToIncrement, combination.get(indexToIncrement) + 1);

        for (int i = indexToIncrement + 1; i < combination.size(); i++) {
            combination.set(i, combination.get(i - 1) + 1);
        }

        return true;
    }

    private static int getRightmostIndexToIncrement(
            int totalObjectsCount, List<Integer> combination
    ) {
        for (int i = combination.size() - 1; i >= 0; i--) {
            if (isRightmostToIncrementIndex(i, totalObjectsCount, combination)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isRightmostToIncrementIndex(
            int index, int totalObjectsCount, List<Integer> currentCombination
    ) {
        return currentCombination.get(index) != totalObjectsCount - currentCombination.size() + index;
    }
}
