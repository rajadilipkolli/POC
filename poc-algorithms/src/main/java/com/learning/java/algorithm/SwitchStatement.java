package com.learning.java.algorithm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwitchStatement {

    static int count = 8;

    public static void main(String[] args) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            switch (count) {
                case 8:
                    str.append("8 ");
                case 9:
                    str.append("9 ");
                case 10: {
                    str.append("10 ");
                    break;
                }
                case 13:
                    str.append("13 ");
                default:
                    str.append("d ");
            }
            count++;
        }
        log.info("String :{}", str);
    }

    static {
        count++;
    }
}
