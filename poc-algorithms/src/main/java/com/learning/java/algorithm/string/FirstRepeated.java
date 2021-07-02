package com.learning.java.algorithm.string;

public class FirstRepeated {

    public static void main(String[] args) {

        String str = "abcfdeacf";
        System.out.println("Output -> " + repeatedAt(str));
    }

    private static String repeatedAt(String str) {

        boolean[] asciiArray = new boolean[256];

        for (int i = 0; i < str.length(); i++) {
            char asciiArrayPosition = str.charAt(i);
            if (asciiArray[asciiArrayPosition]) {
                return "char = " + str.charAt(i) + ", index=" + i;
            }
            asciiArray[asciiArrayPosition] = true;
        }
        return "No repeated values found";
    }
}
