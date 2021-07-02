package com.learning.java.algorithm;

import java.util.LinkedHashMap;

public class RepeatedWord {
    public static void main(String args[]) {
        String s = "Ravi had been saying that he had been there";
        String[] stra = s.split(" ");
        LinkedHashMap<String, Integer> strMap = new LinkedHashMap<>();
        String word = null;
        for (String s1 : stra) {
            if (strMap.get(s1) == null) strMap.put(s1, 1);
            else {
                word = s1;
                break;
            }
        }
        System.out.print(word);
    }
}
