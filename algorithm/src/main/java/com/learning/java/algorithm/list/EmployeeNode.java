package com.learning.java.algorithm.list;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeNode {

	private Employee employee;

	private EmployeeNode next;

	public EmployeeNode(Employee employee) {
		this.employee = employee;
	}

	public String toString() {
		return employee.toString();
	}

}
