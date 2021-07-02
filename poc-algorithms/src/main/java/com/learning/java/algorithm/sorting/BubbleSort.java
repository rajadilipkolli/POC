package com.learning.java.algorithm.sorting;

import java.util.Arrays;

/** Time Complexity is O(n2) , not preferable and it is stable sort. */
public class BubbleSort {

    public static void main(String[] args) {
        int[] intArray = {20, 35, -15, 7, 55, 1, -22};

        bubbleSort(intArray, intArray.length);
    }

    private static void bubbleSort(int[] arr, int lastUnsortedPartitionIndex) {
        if (lastUnsortedPartitionIndex > 1) {
            for (int i = 0; i < lastUnsortedPartitionIndex - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i + 1];
                    arr[i + 1] = arr[i];
                    arr[i] = temp;
                }
            }
            lastUnsortedPartitionIndex--;
            System.out.println(Arrays.toString(arr));
            bubbleSort(arr, lastUnsortedPartitionIndex);
        }
    }
}
