package com.learning.java.algorithm.sorting;

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {

        int[] intArray = {20, 35, -15, 7, 55, 1, -22};

        quickSort(intArray, 0, intArray.length);
    }

    private static void quickSort(int[] intArray, int startPoint, int endPoint) {
        if (endPoint - startPoint < 2) {
            return;
        }

        int pivotIndex = partition(intArray, startPoint, endPoint);
        quickSort(intArray, startPoint, pivotIndex);
        quickSort(intArray, pivotIndex + 1, endPoint);
    }

    private static int partition(int[] intArray, int i, int j) {

        int pivot = intArray[i];

        while (i < j) {

            while (i < j && intArray[--j] >= pivot)
                ;
            if (i < j) {
                intArray[i] = intArray[j];
            }
            while (i < j && intArray[++i] <= pivot)
                ;
            if (i < j) {
                intArray[j] = intArray[i];
            }
        }
        intArray[j] = pivot;
        System.out.println(Arrays.toString(intArray));
        return j;
    }
}
