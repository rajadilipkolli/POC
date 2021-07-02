package com.learning.java.algorithm;

public class LinkedList {

    Node head;

    static class Node {

        int data;
        Node next;

        // Constructor
        Node(int d) {
            data = d;
            next = null;
        }
    }

    public static LinkedList insert(LinkedList list, int data) {
        Node new_node = new Node(data);
        new_node.next = null;

        if (list.head == null) list.head = new_node;
        else {
            Node last = list.head;
            while (last.next != null) {
                last = last.next;
            }

            // Insert the new_node at last node
            last.next = new_node;
        }
        return list;
    }

    public static LinkedList remove(LinkedList list, int data) {

        if (list.head == null) System.out.println("list is empty");
        else {
            Node last = list.head;
            if (last.data == data) {
                Node head = last.next;
                list.head = head;
            } else {
                Node prev = last;
                last = last.next;

                while (last != null) {

                    if (last.data == data) {
                        prev.next = last.next;
                        break;

                    } else {
                        prev = last;
                        last = last.next;
                    }
                }
            }
        }
        return list;
    }

    public static void print(LinkedList list) {

        if (list.head == null) System.out.println("list is empty");
        else {
            Node last = list.head;
            while (last.next != null) {
                System.out.println(last.data);
                last = last.next;
            }
            System.out.println(last.data);
            // Insert the new_node at last node

        }
    }

    public static void main(String args[]) {
        LinkedList lst = new LinkedList();
        lst.insert(lst, 1);
        lst.insert(lst, 2);
        lst.insert(lst, 3);
        lst.print(lst);
        lst.remove(lst, 2);
        lst.remove(lst, 3);
        lst.remove(lst, 1);
        lst.print(lst);
    }
}
