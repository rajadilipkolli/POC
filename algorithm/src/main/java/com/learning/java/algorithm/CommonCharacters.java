package com.learning.java.algorithm;

import java.util.ArrayList;
import java.util.List;

public class CommonCharacters {

    public static void main(String[] args) {
        String s = "geeksforgeeks";
        String s1 = "platformforgeeks";

        char[] ch1 = s.toCharArray();
        char[] ch2 = s1.toCharArray();
        List<Integer> containsList = new ArrayList<>();
        int count = 0;
        for (int i = 0; i <= ch1.length - 1; i++) {
            for (int j = 0; j <= ch2.length - 1; j++) {
                if (ch1[i] == ch2[j] && !containsList.contains(j)) {
                    containsList.add(j);
                    count++;
                    break;

                }
            }
        }

        System.out.println(count);
    }
}
