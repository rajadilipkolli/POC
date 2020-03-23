package com.learning.java.algorithm.string;

import java.util.stream.IntStream;

public class CountOccurrenceOfCharInRepeatedString {

	public static void main(String[] args) {
		int n = 10;
		String str = "abcac";
		char findCountOfChar = 'a';

		System.out.println("Output -> " + countChar(str, findCountOfChar, n));
	}

	private static int countChar(String str, char findCountOfChar, int n) {
		return (int) IntStream.rangeClosed(0, n).filter(i -> str.charAt(i % str.length()) == findCountOfChar).count();
	}

}
