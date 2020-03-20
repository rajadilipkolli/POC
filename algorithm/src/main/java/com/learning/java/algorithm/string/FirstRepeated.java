package com.learning.java.algorithm.string;

public class FirstRepeated {

  public static void main(String[] args) {

    String str = "abcfdeacf";
    System.out.println("Output -> " + repeatedAt(str));
  }

  private static String repeatedAt(String str) {

    int[] asciiArray = new int[256];

    for (int i = 0; i < str.length(); i++) {
      char asciiArrayPosition = str.charAt(i);
      if (asciiArray[asciiArrayPosition] == 1) {
        return "char = "+ str.charAt(i) +", index=" +i;
      }
      asciiArray[asciiArrayPosition]++;
    }
    return "No repeated values found";
  }
}
