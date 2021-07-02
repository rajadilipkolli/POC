package com.learning.java.algorithm.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestSortingByCustomComparator {

    public static void main(String[] args) {

        List<String> s1 = new ArrayList<>();
        s1.add("6");
        s1.add("54");
        s1.add("548");
        s1.add("48");
        s1.add("4");
        s1.add("1");
        s1.add("10");

        Comparator<String> byName = (o1, o2) -> (o2 + o1).compareTo((o1 + o2));
        s1.sort(byName);
        s1.forEach(System.out::println);
    }
}
