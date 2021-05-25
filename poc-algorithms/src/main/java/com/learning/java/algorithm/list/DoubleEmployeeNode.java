package com.learning.java.algorithm.list;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoubleEmployeeNode {

    private final Employee employee;

    private DoubleEmployeeNode next;

    private DoubleEmployeeNode previous;

    public DoubleEmployeeNode(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return employee.toString();
    }
}
