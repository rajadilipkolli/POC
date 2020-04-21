package com.learning.java.algorithm;

public class BinaryTreeMain {

    public static void main(String[] args) {
        BinaryTree br = new BinaryTree();
        br.add(5);
        br.add(3);
        br.add(2);
        br.add(10);
        br.add(15);
        br.add(9);

        br.delete(10);

        br.add(1);


    }
}
