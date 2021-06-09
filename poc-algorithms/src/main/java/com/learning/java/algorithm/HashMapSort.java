package com.learning.java.algorithm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HashMapSort {

    public static void main(String[] args) {
        Employee e11 = new Employee();
        e11.setName("b1");
        e11.setAge(13);

        Employee e21 = new Employee();
        e21.setName("a2");
        e21.setAge(12);

        Employee e31 = new Employee();
        e31.setName("c3");
        e31.setAge(14);

        HashMap<String, Employee> hme = new HashMap<>();
        hme.put("t31", e11);
        hme.put("t21", e21);
        hme.put("t11", e31);
        Map m =
                hme.entrySet().stream()
                        .sorted((e1, e2) -> e1.getValue().getAge() - e2.getValue().getAge())
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldValue, newValue) -> oldValue,
                                        LinkedHashMap::new));

        m.keySet().forEach(System.out::println);
    }
}
