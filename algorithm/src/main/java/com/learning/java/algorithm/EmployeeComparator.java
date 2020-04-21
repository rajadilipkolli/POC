package com.learning.java.algorithm;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee o1, Employee o2) {
        if (o1.getAge() > o2.getAge()) return 1;
        else if (o1.getAge() < o2.getAge()) return -1;
        else return 0;
    }

}
