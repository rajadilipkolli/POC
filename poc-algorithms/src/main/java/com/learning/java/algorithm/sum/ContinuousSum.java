package com.learning.java.algorithm.sum;

public class ContinuousSum {
    public static void main(String[] args) {
        int[] a = {-2, -3, 4, -1, -3, 1, 4, -3};
        int prevSum = 0;
        int startIndex = 0;
        int endIndex = 0;
        int negativeSum = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 0) {
                int sum = a[i];
                for (int j = i + 1; j < a.length - 1; j++) {
                    sum = sum + a[j];
                    if (a[j] < 0) {
                        negativeSum = negativeSum + a[j];
                        if (a[j + 1] > 0 && Math.abs(negativeSum) >= a[i]) {
                            break;
                        }
                    }
                    if (a[j] > 0 && a[j + 1] < 0) {
                        endIndex = j;
                        break;
                    }
                }
                if (prevSum < sum) {
                    prevSum = sum;
                    startIndex = i;
                }
            }
        }
        System.out.println(
                "largest sum "
                        + prevSum
                        + " starting index at "
                        + startIndex
                        + " ending index at "
                        + endIndex);
    }
}
