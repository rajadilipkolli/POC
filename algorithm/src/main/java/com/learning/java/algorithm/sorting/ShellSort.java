package com.learning.java.algorithm.sorting;

import java.util.Arrays;

/**
 * Continuation of Insertion Sort, in-place algorithm, worst case is O(n2), unstable,
 * reduces number of shifting and when gap is 1, perform insertion sort.
 * <p>
 * This can be implemented with BubbleSort as well.
 */
public class ShellSort {

	public static void main(String[] args) {

		int[] intArray = { 20, 35, -15, 7, 55, 1, -22, 100, -55, 459, 0, -300, 44 };

		for (int gap = intArray.length / 2; gap > 0; gap /= 2) {

			for (int i = gap; i < intArray.length; i++) {
				int newElement = intArray[i];

				int j = i;

				while (j >= gap && intArray[j - gap] > newElement) {
					intArray[j] = intArray[j - gap];
					j = j - gap;
				}

				intArray[j] = newElement;

			}
			System.out.println(Arrays.toString(intArray));
		}
	}

}
