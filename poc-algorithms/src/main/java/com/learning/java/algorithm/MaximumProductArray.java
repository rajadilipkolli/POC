package com.learning.java.algorithm;

public class MaximumProductArray {
    public static void main(String args[]) {
        int arr[] = {-2, -3, 0, -2, -40};
        int maxSum = Integer.MIN_VALUE;
        int sum = 1;
        for (int i = 0; i < arr.length; i++) {
            sum = sum * arr[i];
            if (maxSum < sum && sum > 0)
                maxSum = sum;

            if (sum == 0) {
                sum = 1;
            }

        }
        System.out.println("sum is" + maxSum);
    }
}
