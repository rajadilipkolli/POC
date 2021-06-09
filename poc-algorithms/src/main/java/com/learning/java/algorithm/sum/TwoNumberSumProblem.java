package com.learning.java.algorithm.sum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoNumberSumProblem {

    public static void main(String[] args) {
        int[] inputArray = new int[] {2, 15, 7, 11};
        int target = 22;
        long startTime = System.nanoTime();
        int[] values = findSumBySorting(inputArray, target);
        long endTime = System.nanoTime();
        System.out.println(
                "TimeComplexity O(nlogn) output numbers "
                        + Arrays.toString(values)
                        + " ,timeTaken "
                        + (endTime - startTime));

        startTime = System.nanoTime();
        values = findSumByHashMap(inputArray, target);
        endTime = System.nanoTime();
        System.out.println(
                "TimeComplexity O(n) output numbers "
                        + Arrays.toString(values)
                        + " ,timeTaken "
                        + (endTime - startTime));
    }

    private static int[] findSumByHashMap(int[] inputArray, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < inputArray.length; i++) {
            int complement = target - inputArray[i];
            if (numMap.containsKey(complement)) {
                return new int[] {numMap.get(complement), i};
            } else {
                numMap.put(inputArray[i], i);
            }
        }
        return new int[2];
    }

    private static int[] findSumBySorting(int[] inputArray, int target) {

        Arrays.sort(inputArray);

        int left = 0;
        int right = inputArray.length - 1;

        while (left < right) {
            if (inputArray[left] + inputArray[right] == target) {
                return new int[] {inputArray[left], inputArray[right]};
            } else if (inputArray[left] + inputArray[right] < target) {
                left++;
            } else {
                right--;
            }
        }

        return new int[2];
    }
}
