package com.learning.java.algorithm;

import java.util.Arrays;

class CommonCharactersWithArrays {

    // Function to return the count of
// valid indices pairs 
    static int countPairs(String s1, int n1,
                          String s2, int n2) {

        // To store the frequencies of characters
        // of string s1 and s2
        int[] freq1 = new int[26];
        int[] freq2 = new int[26];
        Arrays.fill(freq1, 0);
        Arrays.fill(freq2, 0);

        // To store the count of valid pairs
        int i;
        int count = 0;

        // Update the frequencies of
        // the characters of string s1
        for (i = 0; i < n1; i++) {
            freq1[s1.charAt(i) - 'a']++;
        }

        // Update the frequencies of
        // the characters of string s2
        for (i = 0; i < n2; i++) {
            freq2[s2.charAt(i) - 'a']++;
        }

        // Find the count of valid pairs
        for (i = 0; i < 26; i++) {
            count += (Math.min(freq1[i], freq2[i]));
        }

        return count;
    }

    // Driver code
    public static void main(String[] args) {
        String s1 = "geeksforgeeks";
        String s2 = "platformforgeeks";
        int n1 = s1.length();
        int n2 = s2.length();
        System.out.println(countPairs(s1, n1, s2, n2));
    }
} 