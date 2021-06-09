package com.learning.java.algorithm.list;

public class EmployeeDoubleLinkedList {

    private DoubleEmployeeNode head;

    private DoubleEmployeeNode tail;

    private int size;

    public boolean isEmpty() {
        return head == null && tail == null;
    }

    public int getSize() {
        return size;
    }

    public void printList() {
        DoubleEmployeeNode current = head;
        System.out.print("HEAD -> ");
        while (current != null) {
            System.out.print(current);
            System.out.print(" -> ");
            current = current.getNext();
        }
        System.out.println("TAIL");
    }

    public void addToFront(Employee employee) {
        DoubleEmployeeNode node = new DoubleEmployeeNode(employee);
        node.setNext(head);
        if (isEmpty()) {
            tail = node;
        } else {
            head.setPrevious(node);
        }
        head = node;
        size++;
    }

    public void addToEnd(Employee employee) {
        DoubleEmployeeNode node = new DoubleEmployeeNode(employee);
        if (isEmpty()) {
            head = node;
        } else {
            tail.setNext(node);
            node.setPrevious(tail);
        }
        tail = node;
        size++;
    }

    public void removeFromFront() {
        if (isEmpty()) {
            return;
        }
        DoubleEmployeeNode removedNode = head;
        if (head.getNext() == null) {
            tail = null;
        } else {
            head.getNext().setPrevious(null);
        }
        head = head.getNext();
        size--;
        removedNode.setNext(null);
    }

    public void removeFromLast() {
        if (isEmpty()) {
            return;
        }
        DoubleEmployeeNode removedNode = tail;
        if (tail.getPrevious() == null) {
            head = null;
        } else {
            tail.getPrevious().setNext(null);
        }
        tail = tail.getPrevious();
        size--;
        removedNode.setPrevious(null);
    }

    public boolean addBefore(Employee newEmployee, Employee existingEmployee) {

        // return true if you were able to successfully add the employee
        // into the list before the existing employee. Return false
        // if the existing employee doesn't exist in the list
        if (head == null) {
            return false;
        }
        // find the existing employee
        DoubleEmployeeNode current = head;
        while (current != null && !current.getEmployee().equals(existingEmployee)) {
            current = current.getNext();
        }

        if (current == null) {
            return false;
        }

        DoubleEmployeeNode nodeToInsert = new DoubleEmployeeNode(newEmployee);
        nodeToInsert.setNext(current);
        nodeToInsert.setPrevious(current.getPrevious());
        current.setPrevious(nodeToInsert);

        if (head == current) {
            head = nodeToInsert;
        } else {
            nodeToInsert.getPrevious().setNext(nodeToInsert);
        }
        size++;
        return true;
    }
}
