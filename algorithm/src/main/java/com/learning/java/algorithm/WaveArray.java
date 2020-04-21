package com.learning.java.algorithm;

import java.util.Arrays;

/**
 * Swapping alternative values after sorting
 */
public class WaveArray {

    public static void main(String[] args) {
        int[] a = {10, 5, 6, 3, 2, 20, 100, 80};
        Arrays.sort(a);
        System.out.println("Sorted Array " + Arrays.toString(a));
        int j = a.length - 1;
        for (int i = 0; i < a.length / 2; i = i + 2) {
            int temp = a[j];
            a[j] = a[i];
            a[i] = temp;
            j = j - 2;
        }

        for (int value : a) {
            System.out.print(value + ",");
        }
    }
}
