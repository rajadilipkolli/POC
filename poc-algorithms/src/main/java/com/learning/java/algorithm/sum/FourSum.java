package com.learning.java.algorithm.sum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an array S of n integers, are there elements a, b, c, and d in S such that a + b + c + d =
 * target? Find all unique quadruplets in the array which gives the sum of target.
 */
public class FourSum {

    public static void main(String[] args) {
        int[] input = new int[] {1, 0, -1, 0, -2, 2, 3, -3};
        int target = 0;
        List<int[]> allValues = findAllUniqueQuadruplets(input, target);
        for (int[] array : allValues) {
            System.out.println(Arrays.toString(array));
        }
    }

    private static List<int[]> findAllUniqueQuadruplets(int[] input, int target) {
        List<int[]> result = new ArrayList<>();
        if (input == null || input.length < 4) {
            return result;
        }

        Arrays.sort(input);

        for (int i = 0; i < input.length - 3; i++) {
            if (i > 0 && input[i] == input[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < input.length - 2; j++) {
                if (j != i + 1 && input[j] == input[j - 1]) {
                    continue;
                }
                int left = j + 1;
                int right = input.length - 1;
                while (left < right) {
                    if ((input[left] + input[right] + input[i] + input[j]) == target) {
                        result.add(new int[] {input[i], input[j], input[left], input[right]});
                        left++;
                        right--;
                    } else if ((input[left] + input[right] + input[i] + input[j]) < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return result;
    }
}
