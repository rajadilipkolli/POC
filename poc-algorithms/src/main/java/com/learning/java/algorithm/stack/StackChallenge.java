package com.learning.java.algorithm.stack;

import java.util.LinkedList;

public class StackChallenge {

    public static void main(String[] args) {
        // should return true
        System.out.println(checkForPalindrome("abccba"));
        // should return true
        System.out.println(checkForPalindrome("Was it a car or a cat I saw?"));
        // should return true
        System.out.println(checkForPalindrome("I did, did I?"));
        // should return false
        System.out.println(checkForPalindrome("hello"));
        // should return true
        System.out.println(checkForPalindrome("Don't nod"));
    }

    public static boolean checkForPalindrome(String string) {
        var charArray = string.toLowerCase().toCharArray();
        var characterStack = new LinkedList<Character>();
        StringBuilder stringWithNoPunctuations = new StringBuilder();
        for (var ch : charArray) {
            if (ch >= 'a' && ch <= 'z') {
                stringWithNoPunctuations.append(ch);
                characterStack.push(ch);
            }
        }

        StringBuilder reverseString = new StringBuilder(characterStack.size());
        while (!characterStack.isEmpty()) {
            reverseString.append(characterStack.pop());
        }
        return stringWithNoPunctuations.toString().equals(reverseString.toString());
    }
}
