package com.learning.java.algorithm;

public class PalindromeRemoval {
    public static void main(String args[]) {
        String s = "acbcdbaa";
        char[] ch = s.toCharArray();
        int count = 0;
        for (int i = 0, j = ch.length - 1; i < ch.length / 2; i++, j--) {
            if (ch[i] != ch[j] && i + 1 != j) {
                if (ch[i + 1] == ch[j]) {
                    i = i + 1;
                    count++;
                } else if (ch[i] == ch[j - 1]) {
                    count++;
                    j = j - 1;
                } else if (ch[i + 1] != ch[j] && ch[i] != ch[j - 1]) {
                    count = count + 2;
                    i = i + 1;
                    j = j - 1;
                }


            }
            if (ch[i] != ch[j] && i + 1 == j) {
                count++;
            }
        }
        System.out.println("count" + count);
    }
}
