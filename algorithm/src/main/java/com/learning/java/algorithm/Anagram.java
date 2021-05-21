package com.learning.java.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Anagram {

    public static void main(String[] args) {

        twoStrings();
        listOfStrings();

    }

    private static void listOfStrings() {
        List<String> strLst = new ArrayList<>();
        strLst.add("cat");
        strLst.add("dog");
        strLst.add("tac");
        strLst.add("god");
        strLst.add("act");

        List<String> duplicateLst = new ArrayList<>();

        for (String s : strLst) {
            var count = 0;
            for (String s11 : strLst) {
                if (!duplicateLst.contains(s11)) {
                    char[] sc = s.toCharArray();
                    char[] sc1 = s11.toCharArray();
                    Arrays.sort(sc1);
                    Arrays.sort(sc);
                    if (Arrays.equals(sc1, sc)) {
                        duplicateLst.add(s11);
                        count++;

                    }
                }
            }
            log.info("count for string {}  is :{}", s, count > 1 ? count : "" );
        }
    }

    private static void twoStrings() {

         String s1="geeksforgeeks"; String s2="forgeeksgeeks";

         char[] sc=s1.toCharArray(); char[] sc1=s2.toCharArray();
         Arrays.sort(sc1);Arrays.sort(sc);
         log.info("Anagram : {} ",Arrays.equals(sc1,sc));

    }


}
