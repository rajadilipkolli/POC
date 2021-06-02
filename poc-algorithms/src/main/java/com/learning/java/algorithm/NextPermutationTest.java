package com.learning.java.algorithm;

public class NextPermutationTest {
    public static void main(String args[]) {
        NextPermutation nxt = new NextPermutation();
        int a[] = {1, 3, 5, 4, 2};
        nxt.nextPermutation(a);
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
        }
    }
}
