package com.learning.java.algorithm;

public class MissingNumber {
    public static void main(String[] args) {
        int[] a = {5, 7, 9, 11, 15};

        System.out.print(find(a, 5));

    }

    static int find(int A[], int n) {
        // search space is A[low..high]
        int low = 0, high = n - 1;

        // calculate common difference between successive elements
        int d = (A[n - 1] - A[0]) / n;

        // run till search space is exhausted
        while (low <= high) {
            // find middle index
            int mid = high - (high - low) / 2;

            // check difference of mid element with its right neighbor
            if (mid + 1 < n && A[mid + 1] - A[mid] != d)
                return A[mid + 1] - d;

            // check difference of mid element with its left neighbor
            if (mid - 1 >= 0 && A[mid] - A[mid - 1] != d)
                return A[mid - 1] + d;

            // if missing element lies on left sub-array, then we reduce
            // our search space to left sub-array A[low..mid-1]
            if (A[mid] - A[0] != (mid - 0) * d)
                high = mid - 1;

                // if missing element lies on right sub-array, then reduce
                // our search space to right sub-array A[mid+1..high]
            else
                low = mid + 1;
        }
        return 0;

    }

    static void findelement(int a[], int low, int high, int difference) {
        for (int i = low; i <= high - 1; i++) {
            if (a[i + 1] != a[i] + difference) {
                System.out.println(a[i] + difference);
            }
        }
    }
}
