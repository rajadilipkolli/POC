package com.learning.java.algorithm.sorting;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ArraySortFrequency {

    public static void main(String[] args) {
        int[] array = {2, 4, 5, 2, 1, 9, 3, 2, 2, 5, 1, 5};

        Map<Integer, Integer> tr = new HashMap<>();
        for (int i : array) {
            tr.merge(i, 1, Integer::sum);
        }
        Map<Integer, Integer> result2 = new LinkedHashMap<>();
        tr.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));
        int i = 0;
        for (Entry<Integer, Integer> e : result2.entrySet()) {
            int count = 0;
            while (count < e.getValue()) {
                array[i] = e.getKey();
                i++;
                count++;
            }

        }

        for (int value : array) {
            System.out.print(value + "");
        }

    }
}
