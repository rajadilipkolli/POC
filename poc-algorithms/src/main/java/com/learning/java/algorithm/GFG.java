package com.learning.java.algorithm;

class GFG {
    // m is size of coins array
    // (number of different coins)
    static int minCoins(int[] coins, int m, int v) {
        // table[i] will be storing
        // the minimum number of coins
        // required for i value. So
        // table[v] will have result
        int[] table = new int[v + 1];

        // Base case (If given value v is 0)
        table[0] = 0;

        // Initialize all table values as Infinite
        for (int i = 1; i <= v; i++) table[i] = Integer.MAX_VALUE;

        // Compute minimum coins required for all
        // values from 1 to v
        for (int i = 1; i <= v; i++) {
            // Go through all coins smaller than i
            for (int j = 0; j < m; j++)
                if (coins[j] <= i) {
                    int sub_res = table[i - coins[j]];
                    if (sub_res != Integer.MAX_VALUE && sub_res + 1 < table[i]) table[i] = sub_res + 1;
                }
        }
        return table[v];
    }

    // Driver program
    public static void main(String[] args) {
        int[] coins = {9, 6, 5, 1};
        int m = coins.length;
        int v = 11;
        System.out.println("Minimum coins required is " + minCoins(coins, m, v));
    }
}
