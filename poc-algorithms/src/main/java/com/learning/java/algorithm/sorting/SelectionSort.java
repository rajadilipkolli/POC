package com.learning.java.algorithm.sorting;

import java.util.Arrays;

/**
 * Time Complexity is O(n2) , not preferable and it is unstable sort but better than BubbleSort as
 * number of swaps reduce. Number of Iterations = length -2;
 */
public class SelectionSort {

    public static void main(String[] args) {
        int[] intArray = {20, 35, -15, 7, 55, 1, -22};

        selectionSort(intArray);
    }

    private static void selectionSort(int[] arr) {
        for (int largestUnSortedIndex = arr.length - 1; largestUnSortedIndex > 1; largestUnSortedIndex--) {
            int largestValue = 0;
            for (int j = 1; j <= largestUnSortedIndex; j++) {
                if (arr[largestValue] < arr[j]) {
                    largestValue = j;
                }
            }
            swap(arr, largestValue, largestUnSortedIndex);
            System.out.println(Arrays.toString(arr));
        }
    }

    private static void swap(int[] arr, int highestIndex, int i) {
        if (highestIndex == i) {
            return;
        }
        int temp = arr[highestIndex];
        arr[highestIndex] = arr[i];
        arr[i] = temp;
    }
}
