package com.learning.java.algorithm;

import java.util.Arrays;

public class LongestString {
    public static void main(String[] args) {
        String[] arr = {"geeks", "for", "geeksfor", "geeksforgeeks"};
        Arrays.sort(arr);

        String maxString;
        int count = 0;

        int minLength = arr[0].length();

        if (arr[arr.length - 1].length() - minLength >= minLength) {
            for (int i = arr.length - 1; i >= 0; i--) {
                maxString = arr[i];
                String diffString = "";
                for (int j = i - 1; j >= 0; j--) {

                    if (maxString.contains(arr[j]) && maxString.length() > 0) {

                        maxString = maxString.replaceFirst(arr[j], "");

                        diffString = diffString + arr[j];

                    }
                    if (arr[i].equalsIgnoreCase(diffString)) {
                        System.out.println(arr[i].length());
                        count = 1;
                        break;
                    }
                }
                if (count == 1) {
                    break;
                }
            }
        } else {
            System.out.println(-1);
        }
        if (count != 1) {
            System.out.println(-1);
        }
    }
}
