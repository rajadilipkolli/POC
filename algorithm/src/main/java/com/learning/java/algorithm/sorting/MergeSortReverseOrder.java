package com.learning.java.algorithm.sorting;

import java.util.Arrays;

public class MergeSortReverseOrder {

  public static void main(String[] args) {

    int[] intArray = {20, 35, -15, 7, 55, 1, -22};

    if (intArray.length > 2) {
      mergeSort(intArray, 0, intArray.length);
    } else if (intArray.length == 2 && intArray[0] < intArray[1]) {
      int temp = intArray[0];
      intArray[0] = intArray[1];
      intArray[1] = temp;
    }
    System.out.println(Arrays.toString(intArray));
  }

  private static void mergeSort(int[] intArray, int start, int end) {
    if (end - start < 2) {
      return;
    }
    int mid = (start + end) / 2;
    mergeSort(intArray, start, mid);
    mergeSort(intArray, mid, end);
    merge(intArray, start, mid, end);
  }

  private static void merge(int[] input, int start, int mid, int end) {
    if (input[mid - 1] >= input[mid]) {
      return;
    }

    int i = start;
    int j = mid;
    int tempIndex = 0;

    int[] tempArray = new int[end - start];

    while (i < mid && j < end) {
      tempArray[tempIndex++] = input[i] >= input[j] ? input[i++] : input[j++];
    }
    System.arraycopy(input, i, input, start + tempIndex, mid - i);
    System.arraycopy(tempArray, 0, input, start, tempIndex);
  }
}
