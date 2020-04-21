package com.learning.java.algorithm;

public class SegregatePN {
    public static void main(String args[]) {
        int[] a = {9, -3, 5, -2, -8, -6, 1, 3};
        quicksort(a, 0, a.length - 1);


    }

    static void quicksort(int a[], int low, int high) {
        if (low < high) {
            int pi = partition(a, low, high);

        }
        for (int l = 0; l < a.length; l++) {
            System.out.print(a[l]);
        }
    }

    static int partition(int arr[], int low, int high) {
        int pivot = 0;
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than or
            // equal to pivot
            if (arr[j] <= pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
