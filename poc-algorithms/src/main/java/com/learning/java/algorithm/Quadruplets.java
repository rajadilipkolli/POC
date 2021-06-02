package com.learning.java.algorithm;

import java.util.Arrays;

public class Quadruplets {
    public static void main(String args[]) {
        int[] a1 = {2, 7, 4, 0, 9, 5, 1, 3};
        Arrays.sort(a1);
        String res = "";
        int n = 4;
        int sum = 20;
        quadruplets(a1, res, n, sum);
    }

    static void quadruplets(int a[], String res, int n, int sum) {
        if (sum == 0)
            System.out.println(res);

        if (n == 0)
            res = "";

        for (int i = 0; i < a.length; i++) {
            if (n > 0) {
                res = res + a[i];
                quadruplets(a, res, n - 1, sum - a[i]);
            }


        }
    }
}
