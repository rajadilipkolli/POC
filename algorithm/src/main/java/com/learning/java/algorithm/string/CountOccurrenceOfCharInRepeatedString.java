package com.learning.java.algorithm.string;

public class CountOccurrenceOfCharInRepeatedString {

  public static void main(String[] args) {
    int n = 10;
    String str = "abcac";
    char findCountOfChar = 'a';

    System.out.println("Output -> " + countChar(str, findCountOfChar, n));
  }

  private static int countChar(String str, char findCountOfChar, int n) {
    int count = 0;
    for (int i = 0; i < n; i++) {
      if (str.charAt(i % str.length()) == findCountOfChar) {
        count++;
      }
    }
    return count;
  }
}
