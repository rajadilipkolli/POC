package com.learning.java.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapTest {

    public static void main(String[] args) {
        TreeMap<Integer, Employee> hm = new TreeMap();
        Employee e1 = new Employee();
        e1.setName("e1");
        e1.setAge(50);

        Employee e3 = new Employee();
        e1.setName("e3");
        e3.setAge(10);

        Employee e2 = new Employee();
        e1.setName("e2");
        e1.setAge(40);

        hm.put(1, e1);
        hm.put(2, e2);
        hm.put(3, e3);


        ArrayList<Employee> al = new ArrayList(hm.values());
        Collections.sort(al, new MyComparator());

        System.out.println("//sort by values \n");
        for (Employee obj : al) {
            for (Map.Entry<Integer, Employee> map2 : hm.entrySet()) {
                if (map2.getValue().equals(obj)) {
                    System.out.println(map2.getKey() + " " + map2.getValue());
                }
            }
        }
    }
}

class MyComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee o1, Employee o2) {
        return o1.getName().compareTo(o2.getName());
    }
}