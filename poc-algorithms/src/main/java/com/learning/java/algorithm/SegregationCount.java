package com.learning.java.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class SegregationCount {
    public static Integer getCount(Map<Integer, Integer> count, int key) {

        if (!count.containsKey(key)) {
            count.put(key, 0);
        }

        return count.get(key);
    }

    // Function to merge two sorted sub-arrays arr[low .. mid] and
    // arr[mid + 1 .. high]
    public static void merge(int[] arr, int[] aux, int low, int mid, int high, Map<Integer, Integer> count) {
        int k = low, i = low, j = mid + 1;
        int c = 0;

        // run till there are elements in the left and right runs
        while (i <= mid && j <= high) {
            if (arr[i] > arr[j]) {
                // update surpasser count of arr[i]
                count.put(arr[i], getCount(count, arr[i]) + c);

                aux[k++] = arr[i++];
            } else {
                aux[k++] = arr[j++];
                c++;
            }
        }

        // copy remaining elements
        while (i <= mid) {
            count.put(arr[i], getCount(count, arr[i]) + c);
            aux[k++] = arr[i++];
        }

        // don't need to copy second half

        // copy back to the original array to reflect sorted order
        for (i = low; i <= high; i++) {
            arr[i] = aux[i];
        }
    }

    // Function to sort array arr [low..high] in descending order
    public static void mergeSort(int[] arr, int[] aux, int low, int high, Map<Integer, Integer> count) {
        // base case: run size is equal to 1
        if (high == low) {
            return;
        }

        // find mid point
        int mid = (low + ((high - low) >> 1));

        // recursively split runs into two halves until run size == 1,
        // then merge them and return back up the call chain

        mergeSort(arr, aux, low, mid, count);
        mergeSort(arr, aux, mid + 1, high, count);

        merge(arr, aux, low, mid, high, count);
    }

    // Function to find surpasser count for each element of an array
    public static Map<Integer, Integer> getSurpasserCount(int[] A) {
        Map<Integer, Integer> count = new HashMap<>();

        // create two copies of original array
        int[] aux = Arrays.copyOf(A, A.length);
        int[] arr = Arrays.copyOf(A, A.length);

        // sort the array arr[] in descending order using
        // auxiliary array aux[]
        mergeSort(arr, aux, 0, A.length - 1, count);

        return count;
    }

    public static void main(String[] args) {
        int[] A = {4, 6, 3, 9, 7, 10};

        // find surpasser count for array elements
        Map<Integer, Integer> surpasserCount = getSurpasserCount(A);

        for (int value : A) {
            System.out.print(surpasserCount.get(value) + " ");
        }
    }
}
