package com.learning.java.algorithm.string;

public class UniqueStringCaseSensitive {

  private static final int MAX_CHAR = 256;

  public static void main(String[] args) {

    String key = "asdfgA98SDHJKL";

    System.out.println("Is string unique -> " + uniqueCharacters(key));
  }

  private static boolean uniqueCharacters(String key) {

    // If length is greater than 256, some characters must have been repeated
    if (key.length() > MAX_CHAR) {
      return false;
    }

    short[] array = new short[MAX_CHAR];
    // use boolean for memory allocation
    char[] charArray = key.toCharArray();
    for (char c : charArray) {
      char upperValue = Character.toUpperCase(c);
      if (array[upperValue] == 1) {
        return false;
      }
      array[upperValue]++;
    }

    return true;
  }
}
