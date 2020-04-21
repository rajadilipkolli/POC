package com.learning.java.algorithm;

import java.util.Arrays;

public class TwoArraysEqual {
    public static void main(String args[]) {
        int[] a = {1, 2, 5, 4, 0};
        int[] a1 = {2, 4, 5, 1, 10};

        Arrays.sort(a);
        Arrays.sort(a1);

        System.out.println(Arrays.equals(a, a1));
    }
}
