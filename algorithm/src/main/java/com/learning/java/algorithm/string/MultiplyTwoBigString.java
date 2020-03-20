package com.learning.java.algorithm.string;

public class MultiplyTwoBigString {

    public static void main(String[] args) {

        String a = "123";
        String b = "98765";

        long startTime = System.nanoTime();
        System.out.println("Multiply Brute Force Way -> " + multiply(a, b) + ", TimeTaken -> " + (System.nanoTime() - startTime));
        startTime = System.nanoTime();
        System.out.println("Multiply Optimized Way -> " + multiplyOptimized(a, b) + ", TimeTaken -> " + (System.nanoTime() - startTime));
        startTime = System.nanoTime();
        System.out.println("Multiply MostOptimized Way -> " + multiplyMostOptimized(a, b) + ", TimeTaken -> " + (System.nanoTime() - startTime));

    }

    private static String multiplyMostOptimized(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        int[] num = new int[num1.length() + num2.length()];

        int len1 = num1.length(), len2 = num2.length();
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int temp = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                num[i + j] += (temp + num[i + j + 1]) / 10;
                num[i + j + 1] = (num[i + j + 1] + temp) % 10;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i : num) {
            if (sb.length() > 0 || i > 0) {
                sb.append(i);
            }
        }
        return (sb.length() == 0) ? "0" : sb.toString();
    }

    private static String multiplyOptimized(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int[] arr = new int[num1.length() + num2.length()];

        for (int i = num1.length() - 1; i >= 0; i--) {
            int carry = 0;
            for (int j = num2.length() - 1; j >= 0; j--) {
                arr[i + j + 1] += carry + (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                carry = arr[i + j + 1] / 10;
                arr[i + j + 1] %= 10;
            }
            arr[i] = carry;
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < arr.length && arr[i] == 0) {
            i++;
        }
        if (i >= arr.length) {
            return "0";
        }
        for (int j = i; j < arr.length; j++) {
            builder.append(arr[j]);
        }
        return builder.toString();
    }

    private static String multiply(String firstStringArray, String secondStringArray) {

        int[][] tempArray = new int[firstStringArray.length()][secondStringArray.length() + firstStringArray.length()];

        for (int i = firstStringArray.length(); i > 0; i--) {
            int tempValueToAdd = 0;
            int arrayRowIndex = firstStringArray.length() - i;
            int arrayColumnIndex = (firstStringArray.length() + secondStringArray.length()) - (firstStringArray.length() - i);
            for (int j = secondStringArray.length(); j > 0; j--) {
                int a = firstStringArray.charAt(i - 1) - '0';
                int b = secondStringArray.charAt(j - 1) - '0';
                int val = a * b + tempValueToAdd;
                int unitsValue = (val) % 10;
                tempArray[arrayRowIndex][--arrayColumnIndex] = unitsValue;
                tempValueToAdd = val / 10;
            }
            tempArray[arrayRowIndex][--arrayColumnIndex] = tempValueToAdd;
        }

        StringBuilder finalValue = new StringBuilder();

        int carry = 0;
        for (int j = secondStringArray.length() + firstStringArray.length(); j > 0; j--) {
            int sum = 0;
            for (int[] ints : tempArray) {
                sum = sum + ints[j - 1];
            }
            finalValue.insert(0, (sum + carry) % 10);
            carry = (sum + carry) / 10;
        }

        //remove front 0's
        while (finalValue.charAt(0) == '0' && finalValue.length() > 1) {
            finalValue.deleteCharAt(0);
        }
        return finalValue.toString();
    }

}
