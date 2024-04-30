package com.learning.java.algorithm.sum;

public class MaximumContiguousSubArraySum {

    public static void main(String[] args) {
        int[] arr = new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int length = arr.length;

        int currentSum = arr[0];

        int bestSum = arr[0];

        int currentStart = 0;
        int maxStart = 0;
        int maxEnd = 0;

        for (int i = 1; i < length; i++) {

            if (currentSum + arr[i] < arr[i]) {
                currentSum = arr[i];
                currentStart = i;
            } else {
                currentSum = currentSum + arr[i];
            }

            if (bestSum < currentSum) {
                bestSum = currentSum;
                maxStart = currentStart;
                maxEnd = i;
            }
        }

        System.out.println(bestSum);
        System.out.println("Continuous Array is from " + maxStart + " position to " + maxEnd + " position");
    }
}
