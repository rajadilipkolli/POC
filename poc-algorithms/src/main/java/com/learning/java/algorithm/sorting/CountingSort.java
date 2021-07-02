package com.learning.java.algorithm.sorting;

import java.util.Arrays;

public class CountingSort {

    public static void main(String[] args) {

        int[] intArray = {2, 5, 9, 8, 2, 8, 7, 10, 4, 3};

        countingSort(intArray, 1, 10);

        System.out.println(Arrays.toString(intArray));
    }

    private static void countingSort(int[] intArray, int min, int max) {

        int[] countArray = new int[max - min + 1];
        for (int j = 0; j < intArray.length; j++) {
            countArray[intArray[j] - min]++;
        }

        int k = 0;
        for (int i = min; i <= max; i++) {
            while (countArray[i - min] > 0) {
                intArray[k++] = i;
                countArray[i - min]--;
            }
        }
    }
}
