package com.learning.java.algorithm;

import java.util.HashSet;

class TestQueue {

    public static void main(String[] args) {
        HashSet<String> hashSet = new HashSet<>();
        System.out.println(hashSet.add("d1"));

        Queue queue = new Queue(5);
        queue.insert(10);
        queue.insert(20);
        queue.insert(30);
        queue.insert(40);
        queue.insert(50);
        queue.insert(60);
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println(queue.pop());
        System.out.println("head" + queue.head + "rear" + queue.rear);
        queue.insert(60);
        System.out.println(queue.pop());
    }
}
