package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class Leetcode_2950 {

    public static void main(String[] args) {
        Scanner sc=  new Scanner(System.in);
        String word = "bdh";
        Leetcode_2950 obj = new Leetcode_2950();
        System.out.println(obj.countDivisibleSubstrings(word));
    }
    public static int countDivisibleSubstrings(String word) {
        String[] d = {"ab", "cde", "fgh", "ijk", "lmn", "opq", "rst", "uvw", "xyz"};
        int[] mp = new int[27];

        for (int i = 1; i < d.length; i++) {
            for (char c : d[i].toCharArray()) {
                mp[c - 'a'+1] = i;
            }
        }
        System.out.println(Arrays.toString(mp));
        int ans = 0;
        int n = word.length();
        for (int i = 0; i < n; ++i) {
            int s = 0;
            for (int j = i; j < n; j++) {
                s += mp[word.charAt(j) - 'a'+1];
                ans += s % (j - i + 1) == 0 ? 1 : 0;
            }
        }
        return ans;
    }
}
