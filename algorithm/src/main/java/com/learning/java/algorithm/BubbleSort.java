package com.learning.java.algorithm;

import java.util.Arrays;

/**
 * Time Complexity is O(n2) , not preferable and it is stable sort.
 */
public class BubbleSort {

  public static void main(String[] args) {
    int[] arr = new int[7];
    arr[0] = 20;
    arr[1] = 35;
    arr[2] = -15;
    arr[3] = 7;
    arr[4] = 55;
    arr[5] = 1;
    arr[6] = -22;

    bubbleSort(arr, arr.length);
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
