package org.example;

/**
 * LeetCode 378: Kth Smallest Element in a Sorted Matrix
 * 
 * Problem: Given an n x n matrix where each row and column are sorted in ascending order,
 * find the kth smallest element in the matrix.
 * 
 * Approach: Binary Search on Answer Space
 * - Instead of finding the position, we search for the value
 * - We perform binary search between min (top-left) and max (bottom-right) values
 * - For each mid value, count how many elements are <= mid
 * - If count >= k, then the kth smallest exists in [low, mid], so we move high = mid - 1
 * - If count < k, then we need a larger value, so we move low = mid + 1
 * 
 * Time Complexity: O(n * log(max - min))
 * Space Complexity: O(1)
 */
public class Leetcode_378_kth_samllest_matrix {
    
    /**
     * PURPOSE: Find the kth smallest element in a sorted matrix using Binary Search
     * 
     * APPROACH: Binary search on the value range instead of indices
     * - Initialize low to top-left element (minimum) and high to bottom-right (maximum)
     * - Binary search for the smallest value where count of elements <= value >= k
     * 
     * BEFORE EXAMPLE:
     *   matrix = [[1,2],[1,4]], k = 3
     *   Expected: 2
     *   Explanation: The sorted order is [1,1,2,4], and 3rd smallest is 2
     * 
     * AFTER EXAMPLE:
     *   Binary search finds that when mid=2, there are exactly 3 elements <= 2,
     *   so the answer is 2
     */
    public int kthSmallest(int[][] matrix, int k) {
        // Get matrix dimensions
        int m = matrix.length;
        int n = matrix[0].length;
        
        // Initialize search range: low = smallest element, high = largest element
        int low = matrix[0][0];          // Top-left element (minimum in matrix)
        int high = matrix[m-1][n-1];    // Bottom-right element (maximum in matrix)
        int res = -1;                    // Store result
        
        // Binary search for the kth smallest element
        while(low <= high){
            // Calculate mid value to test
            int mid = low + (high - low) / 2;
            
            // Count how many elements are <= mid value
            int ans = lessThan_K(matrix, mid);
            
            // If count < k, then kth smallest is greater than mid
            if(ans < k) {
                low = mid + 1;  // Search in upper half
            }
            // If count >= k, then kth smallest is <= mid
            else {
                res = mid;      // mid could be our answer
                high = mid - 1; // Try to find smaller value
            }
        }
        return res;
    }

    /**
     * PURPOSE: Count total number of elements in matrix that are <= mid value
     * 
     * APPROACH: Start from bottom-left corner, move in a zigzag pattern
     * - If current element <= mid: add all elements above (including current)
     * - If current element > mid: move up to find smaller elements
     * - This works because each row and column is sorted
     * 
     * BEFORE EXAMPLE:
     *   matrix = [[1,2],[1,4]], mid = 2
     *   Count elements <= 2: [1,1,2] = 3 elements
     * 
     * AFTER EXAMPLE:
     *   Starting from (1,0): Check 1 <= 2? Yes, add 2 elements (row 0,1)
     *   Then check (0,1): Check 2 <= 2? Yes, add 1 element (row 0)
     *   Total count = 3
     */
    public int lessThan_K(int[][] matrix, int mid){
        // Start from bottom-left corner (row = m-1, col = 0)
        int row = matrix.length - 1;    // Last row (bottom)
        int col = 0;                    // First column (left)
        int cnt = 0;                    // Counter for elements <= mid
        
        // Traverse matrix in zigzag pattern from bottom-left to top-right
        while(row >= 0 && col < matrix[0].length){
            // If current element is <= mid
            if(matrix[row][col] <= mid) {
                // All elements above current element in same column are also <= mid
                // (because column is sorted), so add row+1 elements
                cnt += row + 1;
                // Move to next column
                col++;
            }
            // If current element > mid, need to check smaller values in upper rows
            else {
                row--;  // Move up
            }
        }
        return cnt;
    }

    /**
     * DRIVER METHOD: Main method to demonstrate the solution with hardcoded test cases
     */
    public static void main(String[] args) {
        Leetcode_378_kth_samllest_matrix solution = new Leetcode_378_kth_samllest_matrix();
        
        System.out.println("========== LeetCode 378: Kth Smallest Element in Sorted Matrix ==========\n");
        
        // ====== TEST CASE 1 ======
        System.out.println("TEST CASE 1:");
        int[][] matrix1 = {
            {1, 2},
            {1, 4}
        };
        int k1 = 3;
        System.out.println("Input Matrix:");
        printMatrix(matrix1);
        System.out.println("K = " + k1);
        int result1 = solution.kthSmallest(matrix1, k1);
        System.out.println("Output: " + result1);
        System.out.println("Expected: 2");
        System.out.println("Explanation: Sorted order is [1,1,2,4], 3rd smallest element is 2\n");
        
        // ====== TEST CASE 2 ======
        System.out.println("TEST CASE 2:");
        int[][] matrix2 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        int k2 = 10;
        System.out.println("Input Matrix:");
        printMatrix(matrix2);
        System.out.println("K = " + k2);
        int result2 = solution.kthSmallest(matrix2, k2);
        System.out.println("Output: " + result2);
        System.out.println("Expected: 10");
        System.out.println("Explanation: Sorted order is [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]\n");
        
        // ====== TEST CASE 3 ======
        System.out.println("TEST CASE 3:");
        int[][] matrix3 = {
            {-7, -5, -3},
            {-6, -4, -1},
            {-4, 0, 3}
        };
        int k3 = 5;
        System.out.println("Input Matrix:");
        printMatrix(matrix3);
        System.out.println("K = " + k3);
        int result3 = solution.kthSmallest(matrix3, k3);
        System.out.println("Output: " + result3);
        System.out.println("Expected: -4");
        System.out.println("Explanation: With negative numbers, 5th smallest is -4\n");
        
        // ====== TEST CASE 4 ======
        System.out.println("TEST CASE 4:");
        int[][] matrix4 = {
            {1, 3, 5},
            {6, 8, 10},
            {11, 13, 15}
        };
        int k4 = 1;
        System.out.println("Input Matrix:");
        printMatrix(matrix4);
        System.out.println("K = " + k4);
        int result4 = solution.kthSmallest(matrix4, k4);
        System.out.println("Output: " + result4);
        System.out.println("Expected: 1");
        System.out.println("Explanation: 1st smallest element is always the top-left element\n");
        
        // ====== TEST CASE 5 ======
        System.out.println("TEST CASE 5:");
        int[][] matrix5 = {
            {1, 5, 9},
            {10, 11, 13},
            {12, 13, 15}
        };
        int k5 = 8;
        System.out.println("Input Matrix:");
        printMatrix(matrix5);
        System.out.println("K = " + k5);
        int result5 = solution.kthSmallest(matrix5, k5);
        System.out.println("Output: " + result5);
        System.out.println("Expected: 13");
        System.out.println("Explanation: 8th smallest element is 13\n");
        
        // ====== SUMMARY ======
        System.out.println("========== TEST SUMMARY ==========");
        System.out.println("Test Case 1: " + (result1 == 2 ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 2: " + (result2 == 10 ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 3: " + (result3 == -4 ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 4: " + (result4 == 1 ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 5: " + (result5 == 13 ? "✓ PASS" : "✗ FAIL"));
    }
    
    /**
     * HELPER METHOD: Print matrix in readable format
     */
    private static void printMatrix(int[][] matrix) {
        for(int[] row : matrix) {
            System.out.print("[");
            for(int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if(i < row.length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }
}
