package com.learning.java.algorithm.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string containing lowercase characters. The task is to print the maximum
 * occurring character in the input string. If 2 or more characters appear the same number
 * of times, print the lexicographically (alphabetically) lowest (first) character.
 */
public class MaximumOccurringCharacterInAnInputString {

	private static final int ASCII_SIZE = 256;

	public static void main(String[] args) {
		String str = "helloworld";
		System.out.println("Max occurring character is " + getMaxOccurringChar(str));

		String anotherString = "test sample";
		System.out.println("Max occurring character in lexicographically is"
				+ getMaxOccurringCharsLexicographically(anotherString.toCharArray()));

		anotherString = "bbaaabccc";
		System.out.println("Max occurring character which occurs first is " + getMaxOccurringCharFirst(anotherString));
	}

	private static char getMaxOccurringCharFirst(String anotherString) {
		int[] count = new int[ASCII_SIZE];
		char result = anotherString.charAt(0);
		int max = -1;
		for (int i = 0; i < anotherString.length(); i++) {
			count[anotherString.charAt(i)]++;
			if (max < count[anotherString.charAt(i)]) {
				max = count[anotherString.charAt(i)];
			}
		}

		for (int i = 0; i < anotherString.length(); i++) {
			if (max == count[anotherString.charAt(i)]) {
				result = anotherString.charAt(i);
				break;
			}
		}
		return result;
	}

	private static List<Character> getMaxOccurringCharsLexicographically(char[] anotherString) {
		int[] count = new int[26];

		int max = -1;
		for (char c : anotherString) {
			if (c != ' ') {
				int countValue = ++count[c - 'a'];
				if (max < countValue) {
					max = countValue;
				}
			}
		}

		List<Character> results = new ArrayList<>();

		for (int i = 0; i < 26; i++) {
			if (count[i] == max) {
				results.add((char) (i + 'a'));
			}
		}

		return results;
	}

	private static Character getMaxOccurringChar(String str) {
		// Create array to keep the count of individual
		// characters and initialize the array as 0
		int[] count = new int[ASCII_SIZE];

		// Construct character count array from the input string.
		int max = -1;
		char result = ' ';
		int len = str.length();
		for (int i = 0; i < len; i++) {
			count[str.charAt(i)]++;
			if (max < count[str.charAt(i)]) {
				max = count[str.charAt(i)];
				result = str.charAt(i);
			}
		}
		return result;
	}

}
