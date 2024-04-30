package com.learning.java.algorithm;

class NdigitNumber {

    // Function to find all N-digit numbers with equal sum of digits at even
    // and odd index in Bottom-up manner
    public static void findNdigitNums(String res, int n, int diff) {
        // if number is less than N-digit
        if (n > 0) {
            char ch = '0';

            // special case - number can't start from 0
            if (res.equals("")) {
                ch = '1';
            }

            // consider every valid digit and put it in the current
            // index and recur for next index
            while (ch <= '9') {
                int absdiff;

                // update difference between odd and even digits
                if ((n & 1) != 0) {
                    // add value to diff if odd digit
                    absdiff = diff - (ch - '0');
                } else {
                    // subtract value from diff if even
                    absdiff = diff + (ch - '0');
                }

                findNdigitNums(res + ch, n - 1, absdiff);
                ch++;
            }
        }
        // if number becomes N-digit with equal sum of even and odd
        // digits, print it
        else if (n == 0 && Math.abs(diff) == 0) {
            System.out.println(res);
        }
    }

    public static void main(String[] args) {

        int n = 2; // N-digit
        String res = "";

        findNdigitNums(res, n, 0);
    }
}
