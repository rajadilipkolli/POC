package com.learning.java.algorithm;

class Main {
    // Function to count the number of strictly increasing sub-arrays in an array
    public static int getCount(int[] arr) {
        // stores the count of strictly increasing sub-arrays
        int count = 0;

        // stores the length of current strictly increasing sub-array
        int len = 1;

        // traverse the array from left to right starting from the 1st index
        for (int i = 1; i < arr.length; i++) {
            // if previous element is less than the current element
            if (arr[i - 1] < arr[i]) {
                // add the length of current strictly increasing sub-array
                // to the answer and increment it
                count += (len++);
            } else {
                // reset the length to 1
                len = 1;
            }
        }

        // return the count of strictly increasing sub-arrays
        return count;
    }

    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};

        System.out.print("The number of strictly increasing sub-arrays are "
                + getCount(arr));
    }
}