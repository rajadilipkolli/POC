package com.learning.java.algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleRounding {

    public static void main(String[] args) {
        //        Double variable =  Double.valueOf("0.0119999");
        double variable = 0.145D;
        double d = variable * 100;
        System.out.println(d);
        BigDecimal result = BigDecimal.valueOf(variable).multiply(BigDecimal.valueOf(100));
        // Set scale to 2 and use ROUND_DOWN to truncate
        System.out.println(result.setScale(2, RoundingMode.DOWN));
    }
}
