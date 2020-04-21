package com.learning.java.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class KClosestNumbers {
    public static void main(String args[]) {
        int a[] = {-10, -50, 20, 17, 80};
        Arrays.sort(a);
        int x1 = 20;
        int k = 2;

        HashMap<Integer, Integer> hm = new HashMap<>();
        for (int i : a) {
            hm.put(i, Math.abs(i - x1));
        }
        Map<Integer, Integer> result2 = new LinkedHashMap<>();
        hm.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue())
                .forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));

        int count = 0;
        for (Integer key : result2.keySet()) {
            if (count == k)
                break;
            System.out.println(key);
            count++;

        }
    }
}
