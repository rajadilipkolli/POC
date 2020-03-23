package com.learning.java.algorithm.searching;

public class Search2DMatrix {

	public static void main(String[] args) {
		int[][] matrix = { { 1, 5, 9, 11 }, { 13, 16, 19, 24 }, { 28, 30, 38, 50 } };
		int target = 38;
		System.out.println(searchMatrix(matrix, target));
	}

	private static boolean searchMatrix(int[][] matrix, int target) {
		if (matrix.length == 0) {
			return false;
		}
		int numOfRows = matrix.length;
		int numOfCols = matrix[0].length;

		int left = 0, right = numOfRows * numOfCols - 1;

		while (left <= right) {
			int mid = (left + right) / 2;
			int midValue = matrix[mid / numOfCols][mid % numOfCols];
			if (target == midValue) {
				return true;
			}
			else if (target < midValue) {
				right = mid - 1;
			}
			else {
				left = mid + 1;
			}
		}

		return false;
	}

}
