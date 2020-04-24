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
}
