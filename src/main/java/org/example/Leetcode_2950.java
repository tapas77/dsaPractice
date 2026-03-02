package org.example;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Leetcode Problem 2950: Count Divisible Substrings
 *
 * Problem: Count the number of substrings where the sum of character values is divisible by the substring length.
 *
 * Approach:
 * 1. Each letter is assigned a value based on which group it belongs to:
 *    - Group 1: "ab" (a=1, b=1)
 *    - Group 2: "cde" (c=2, d=2, e=2)
 *    - Group 3: "fgh" (f=3, g=3, h=3)
 *    - ... and so on up to group 9: "xyz"
 * 2. For each possible starting position i, calculate the sum of values for all substrings starting at i
 * 3. A substring is divisible if sum % length == 0
 */
public class Leetcode_2950 {

    /**
     * Main method to test the solution with hardcoded input
     */
    public static void main(String[] args) {
        // Initialize scanner (not used in this example but present in original)
        Scanner sc = new Scanner(System.in);

        // Test input: "bdh"
        String word = "bdh";
        Leetcode_2950 obj = new Leetcode_2950();

        // Print the result
        System.out.println("Count of divisible substrings: " + obj.countDivisibleSubstrings(word));
    }

    /**
     * Counts the number of substrings where sum of character values is divisible by substring length
     *
     * @param word the input string containing lowercase letters
     * @return the count of divisible substrings
     *
     * Time Complexity: O(n^2) where n is the length of the word
     * Space Complexity: O(1) for the mapping array (fixed size 27)
     */
    public static int countDivisibleSubstrings(String word) {
        // Define character groups: each group has a unique value (1-9)
        // Group 1: "ab", Group 2: "cde", Group 3: "fgh", etc.
        String[] d = {"ab", "cde", "fgh", "ijk", "lmn", "opq", "rst", "uvw", "xyz"};

        // Create a mapping array where index represents character position and value is the group number
        int[] mp = new int[27];

        // Fill the mapping array: assign each character its group value
        // i represents the group number (1-indexed)
        // For example: a, b -> 1; c, d, e -> 2; f, g, h -> 3, etc.
        for (int i = 1; i < d.length; i++) {
            for (char c : d[i].toCharArray()) {
                // Convert character to index (a=1, b=2, ..., z=26)
                // Assign group number i to this character
                mp[c - 'a' + 1] = i;
            }
        }

        // Debug: print the mapping array
        System.out.println("Character to group mapping: " + Arrays.toString(mp));

        int ans = 0;
        int n = word.length();

        // Iterate through all possible starting positions
        for (int i = 0; i < n; ++i) {
            // Sum of character values from position i to j
            int s = 0;

            // Iterate through all ending positions j starting from i
            for (int j = i; j < n; j++) {
                // Add the value of character at position j to the sum
                s += mp[word.charAt(j) - 'a' + 1];

                // Check if the sum is divisible by the substring length (j - i + 1)
                // If divisible, increment the count
                ans += s % (j - i + 1) == 0 ? 1 : 0;
            }
        }

        return ans;
    }
}
