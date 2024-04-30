package com.learning.java.algorithm;

public class NGE {

    public static void main(String[] args) {
        int[] arr = {11, 13, 21, 3};

        for (int i = 0; i < arr.length; i++) {
            int next = -1;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    next = arr[j];

                    break;
                }
            }
            System.out.println(arr[i] + " next element " + next);
        }
    }
}
