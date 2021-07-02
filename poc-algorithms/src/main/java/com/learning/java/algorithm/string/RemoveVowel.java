package com.learning.java.algorithm.string;

import java.util.Arrays;
import java.util.List;

public class RemoveVowel {

    public static void main(String[] args) {
        String str = "GeeeksforGeeks - A Computer Science Portal for Geeks";

        System.out.println(removeVowel(str));

        System.out.println(removeConsecutiveVowels(str));
    }

    private static String removeConsecutiveVowels(String str) {
        Character[] vowels = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};

        List<Character> al = Arrays.asList(vowels);

        StringBuilder sb = new StringBuilder(str);

        for (int i = 0; i < sb.length() - 1; i++) {
            if (al.contains(sb.charAt(i)) && al.contains(sb.charAt(i + 1))) {
                int endPosition = i + 2;
                for (int j = i + 2; j < sb.length(); j++) {
                    if (al.contains(sb.charAt(j))) {
                        endPosition++;
                    } else {
                        break;
                    }
                }
                sb.replace(i, endPosition, "");
                i = i - 2;
            }
        }
        return sb.toString();
    }

    private static String removeVowel(String str) {
        Character[] vowels = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};

        List<Character> al = Arrays.asList(vowels);

        StringBuilder sb = new StringBuilder(str);

        for (int i = 0; i < sb.length(); i++) {

            if (al.contains(sb.charAt(i))) {
                sb.replace(i, i + 1, "");
                i--;
            }
        }

        return sb.toString();
    }
}
