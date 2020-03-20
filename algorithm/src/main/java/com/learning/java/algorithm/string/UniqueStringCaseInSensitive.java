package com.learning.java.algorithm.string;

public class UniqueStringCaseInSensitive {

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
    for (int i = 0; i < key.length(); i++) {
      int arrayPosition = key.charAt(i);
      if (array[arrayPosition] == 1) {
        return false;
      }
      array[arrayPosition]++;
    }

    return true;
  }
}
