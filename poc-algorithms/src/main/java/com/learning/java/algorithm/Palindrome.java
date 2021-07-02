package com.learning.java.algorithm;

public class Palindrome {
    public static void main(String args[]) {
        int n = 265;
        int k = reverse(n);
        while (k != n) {
            n = k + n;
            k = reverse(n);
        }
        System.out.print(n);
    }

    static int reverse(int i) {
        int m;
        int k = 0;
        while (i > 0) {
            m = i % 10;
            i = i / 10;
            k = k * 10 + m;
        }
        return k;
    }
}
