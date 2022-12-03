package com.learning.java.algorithm.sorting;

import java.util.Arrays;

public class InsertionSortRecurssion {

    public static void main(String[] args) {

        int[] intArray = {20, 35, -15, 7, 55, 1, -22};

        insertionSort(intArray, 1);

        System.out.println(Arrays.toString(intArray));
    }

    private static void insertionSort(int[] intArray, int firstUnsortedIndex) {
        if (firstUnsortedIndex == intArray.length) {
            return;
        }
        int newElement = intArray[firstUnsortedIndex];
        int i;
        for (i = firstUnsortedIndex; i > 0 && intArray[i - 1] > newElement; i--) {
            intArray[i] = intArray[i - 1];
        }
        intArray[i] = newElement;
        insertionSort(intArray, ++firstUnsortedIndex);
    }
}
