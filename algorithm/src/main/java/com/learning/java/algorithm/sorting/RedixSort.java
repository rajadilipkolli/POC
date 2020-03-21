package com.learning.java.algorithm.sorting;

import java.util.Arrays;

/**
 * Stable Sort
 */
public class RedixSort {
  public static void main(String[] args) {

    int[] radixArray = { 4725, 4586, 1330, 8792, 1594, 5729 };

    // execution step 1 -> oth position  --> 1330, 8792, 1594, 4725, 4586, 5729
    // execution step 2 -> 1th position  --> 4725, 5729, 1330, 4586, 8792, 8792
    // execution step 3 -> 2nd position  --> 1330, 4586, 1594, 4725, 5729, 8792
    // execution step 4 -> 3rd position  --> 1330, 1594, 4586, 4725, 5729, 8792
    radixSort(radixArray, 10, 4);

  }

  public static void radixSort(int[] input, int radix, int width) {
    for (int i = 0; i < width; i++) {
      radixSingleSort(input, i, radix);
    }
  }

  public static void radixSingleSort(int[] input, int position, int radix) {

    int numItems = input.length;
    int[] countArray = new int[radix];

    for (int value : input) {
      countArray[getDigit(position, value, radix)]++;
    }
    // Adjust the count array
    for (int j = 1; j < radix; j++) {
      countArray[j] += countArray[j - 1];
    }

    int[] temp = new int[numItems];
    for (int tempIndex = numItems - 1; tempIndex >= 0; tempIndex--) {
      temp[--countArray[getDigit(position, input[tempIndex], radix)]] = input[tempIndex];
    }

    System.arraycopy(temp, 0, input, 0, numItems);

    System.out.println(Arrays.toString(input));
  }

  public static int getDigit(int position, int value, int radix) {
    return value / (int) Math.pow(radix, position) % radix;
  }
}
