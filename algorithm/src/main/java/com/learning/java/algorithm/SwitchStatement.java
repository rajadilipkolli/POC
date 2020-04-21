package com.learning.java.algorithm;

public class SwitchStatement {

    static int count = 8;

    public static void main(String[] args) {
        String str = "";
        for (int i = 0; i < 3; i++) {
            switch (count) {
                case 8:
                    str += "8 ";
                case 9:
                    str += "9 ";
                case 10: {
                    str += "10 ";
                    break;
                }
                default:
                    str += "d ";
                case 13:
                    str += "13 ";
            }
            count++;
        }
        System.out.println(str);
    }

    static {
        count++;
    }
}