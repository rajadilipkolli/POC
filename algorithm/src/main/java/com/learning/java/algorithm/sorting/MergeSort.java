package com.learning.java.algorithm.sorting;

import java.util.Arrays;

/**
 * Stable algorithm, O(nlogn) complexity as we are repeatedly dividing the array in half
 * during splitting phase. Not an in place sorting as it needs temporary array
 */
public class MergeSort {

  public static void main(String[] args) {

    int[] intArray = { 20, 35, -15, 7, 55, 1, -22 };

    if (intArray.length > 2) {
      mergeSort(intArray, 0, intArray.length);
    }
  }

  public static void mergeSort(int[] input, int start, int end) {
    if (end - start < 2) {
      return;
    }
    int mid = (start + end) / 2;
    mergeSort(input, start, mid);
    mergeSort(input, mid, end);
    merge(input, start, mid, end);
    System.out.println(Arrays.toString(input));
  }

  private static void merge(int[] input, int start, int mid, int end) {
    if (input[mid - 1] <= input[mid]) {
      return;
    }
    int i = start;
    int j = mid;
    int tempIndex = 0;

    int[] tempArray = new int[end - start];

    while (i < mid && j < end) {
      tempArray[tempIndex++] = input[i] <= input[j] ? input[i++] : input[j++];
    }
    System.arraycopy(input, i, input, start + tempIndex, mid - i);
    System.arraycopy(tempArray, 0, input, start, tempIndex);
  }
}
