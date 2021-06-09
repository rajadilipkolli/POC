package com.learning.java.algorithm;

public class Queue {

    int head;
    int rear;
    int capacity;
    int[] array;

    public Queue(int capacity) {
        this.capacity = capacity;
        head = -1;
        rear = -1;
        array = new int[this.capacity];
    }

    boolean isFull() {
        return head == capacity - 1;
    }

    boolean isEmpty() {
        return head == -1;
    }

    int insert(int element) {
        if (isFull()) {
            System.out.println("queue is full");
            return -1;
        } else {

            array[++head] = element;

            return element;
        }
    }

    int pop() {
        if (isEmpty()) {
            rear = -1;
            System.out.println("queue is empty");
            return -1;
        } else {
            head--;
            return array[++rear];
        }
    }
}
