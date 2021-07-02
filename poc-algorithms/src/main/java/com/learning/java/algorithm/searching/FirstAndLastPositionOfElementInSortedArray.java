package com.learning.java.algorithm.searching;

import java.util.Arrays;

public class FirstAndLastPositionOfElementInSortedArray {

    public static void main(String[] args) {
        int[] input = new int[] {5, 7, 7, 8, 8, 10};
        int target = 8;
        int[] response = searchRange(input, target);
        System.out.println(Arrays.toString(response));
    }

    public static int[] searchRange(int[] input, int target) {
        int left = 0;
        int right = input.length - 1;

        while (left < right) {
            int mid = (left + right - 1) / 2;
            if (input[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        int first = left;
        if (left < input.length && input[left] == target) { // left is in boundary and is
            // the target
            left = 0;
            right = input.length - 1;
            while (left < right) {
                int mid = left + (right - left + 1) / 2;

                if (input[mid] > target) {
                    right = mid - 1;
                } else {
                    left = mid;
                }
            }

            return new int[] {first, right};
        }

        return new int[] {-1, -1};
    }
}
