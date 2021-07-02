package com.learning.java.algorithm;

public class SegregateRelative {
    public static void printCombination(int[] out, int n) {
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", out[i]);
        }
        System.out.println();
    }

    // Recursive function to print all combination of numbers from i to n
    // having sum n. index denotes the next free slot in output array out
    public static void recur(int i, int n, int[] out, int index) {
        // if sum becomes n, print the combination
        if (n == 0) {
            printCombination(out, index);
        }

        int j;

        // start from previous element in the combination till n
        for (j = i; j <= n; j++) {
            // place current element at current index
            out[index] = j;

            // recur with reduced sum
            recur(j, n - j, out, index + 1);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        int[] out = new int[n];

        // print all combination of numbers from 1 to n having sum n
        recur(1, n, out, 0);
    }
}
