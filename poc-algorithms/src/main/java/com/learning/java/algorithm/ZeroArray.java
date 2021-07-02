package com.learning.java.algorithm;

public class ZeroArray {

    public static void main(String[] args) {

        int[] arr1 = {0, 2, 2, 2, 0, 6, 6, 0, 0, 8};
        int count1 = 0;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] > 0) {

                if (i < arr1.length - 1 && arr1[i] == arr1[i + 1]) {
                    arr1[count1] = 2 * arr1[i];

                    if (i < arr1.length - 2) i++;
                } else arr1[count1] = arr1[i];
                count1++;
            }
        }

        int[] arr2 = {1, 2, 3, 4, 5};
        int[] arr3 = new int[5];
        for (int i = 0; i < 5; i++) {
            int sum = 1;
            for (int j = 0; j < 5; j++) {
                if (i != j) {
                    sum = sum * arr2[j];
                }
            }
            arr3[i] = sum;
        }

        for (int value : arr3) {
            System.out.println(value);
        }
    }
}
