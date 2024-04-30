package com.learning.java.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NonPrinting {

    public static void main(String[] args) {
        List<String> s1 = new ArrayList<>();
        s1.add("aaa");
        s1.add("bb");
        s1.add("bb");
        s1.add("bb");
        s1.add("cc");
        s1.add("for");
        s1.add("for");
        HashMap<String, Integer> m = new HashMap<>();
        for (String s : s1) {
            if (m.get(s) == null) {
                m.put(s, 1);
            } else {
                m.put(s, m.get(s) + 1);
            }
        }

        String k = returnkey(m);
        m.remove(k);
        String key = returnkey(m);
        System.out.println(key);
    }

    static String returnkey(Map<String, Integer> m) {
        int first = 0;
        String key = null;
        for (Entry<String, Integer> m1 : m.entrySet()) {

            if (m1.getValue() > first) {
                first = m1.getValue();
                key = m1.getKey();
            }
        }
        return key;
    }
}
