package com.learning.java.algorithm.list;

public class DoubleLinkedListMain {

    public static void main(String[] args) {

        Employee janeJones = new Employee("Jane", "Jones", 123);
        Employee johnDoe = new Employee("John", "Doe", 4567);
        Employee marySmith = new Employee("Mary", "Smith", 22);
        Employee mikeWilson = new Employee("Mike", "Wilson", 3245);

        EmployeeDoubleLinkedList list = new EmployeeDoubleLinkedList();

        System.out.println(list.isEmpty());

        list.addToFront(janeJones);
        list.addToFront(johnDoe);
        list.addToFront(marySmith);
        list.addToFront(mikeWilson);

        System.out.println(list.getSize());

        list.printList();

        list.removeFromFront();
        System.out.println(list.getSize());
        list.printList();

        list.addToEnd(mikeWilson);
        System.out.println(list.getSize());
        list.printList();

        System.out.println("Removing from Last");
        list.removeFromLast();
        list.removeFromLast();
        list.removeFromLast();
        list.removeFromLast();

        System.out.println(list.getSize());
        list.printList();

        list.addToFront(janeJones);
        list.addToEnd(johnDoe);
        list.addToFront(marySmith);
        list.addToEnd(mikeWilson);
        System.out.println(list.getSize());
        list.printList();
    }

}
