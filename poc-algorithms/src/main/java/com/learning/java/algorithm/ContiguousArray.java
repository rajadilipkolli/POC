package com.learning.java.algorithm;

public class ContiguousArray {
    public static void main(String[] args) {

        int[] A = {1, 2, 3, 2, 4, 5};

        int count = 0;
        for (int i = 0; i < A.length; i++) {
            if (i <= A.length - 2 && A[i] < A[i + 1]) {
                count++;

                if (i > 0 && A[i] > A[i - 1]) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }
}
