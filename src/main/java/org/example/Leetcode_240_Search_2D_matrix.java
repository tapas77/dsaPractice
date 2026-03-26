package org.example;

/**
 * LeetCode 240: Search a 2D Matrix II
 * 
 * Problem: Write an efficient algorithm that searches for a value target in an m x n 
 * integer matrix. The matrix has the following properties:
 * - Integers in each row are sorted in ascending order (left to right)
 * - Integers in each column are sorted in ascending order (top to bottom)
 * 
 * Example:
 * matrix = [[1,4,7,11,15],
 *          [2,5,8,12,19],
 *          [3,6,9,16,22],
 *          [10,13,14,17,24],
 *          [18,21,23,26,30]]
 * target = 13
 * Output: true
 * 
 * APPROACH: Zigzag Search from Bottom-Left Corner
 * - Start from bottom-left corner (has smallest values in its column, largest in its row)
 * - If target == current: found it, return true
 * - If target > current: move right (larger values in same column)
 * - If target < current: move up (smaller values in same row)
 * - Time Complexity: O(m + n) - we move at most m+n times
 * - Space Complexity: O(1) - only constant extra space
 * 
 * Why does this work?
 * Starting from bottom-left eliminates one row or column per comparison:
 * - If we need larger values, column must increase (move right)
 * - If we need smaller values, row must decrease (move up)
 */
public class Leetcode_240_Search_2D_matrix {
    
    /**
     * PURPOSE: Search for target value in a 2D matrix with sorted rows and columns
     * 
     * ALGORITHM: Zigzag traversal starting from bottom-left corner
     * 1. Initialize pointer at bottom-left: row = m-1, col = 0
     * 2. Compare matrix[row][col] with target:
     *    - If equal: target found, return true
     *    - If current > target: eliminate current row (move up)
     *    - If current < target: eliminate current column (move right)
     * 3. Exit loop when row < 0 or col >= n (out of bounds)
     * 4. Return false if not found
     * 
     * BEFORE EXAMPLE:
     * matrix = [[1,4,7,11,15],
     *          [2,5,8,12,19],
     *          [3,6,9,16,22],
     *          [10,13,14,17,24],
     *          [18,21,23,26,30]]
     * target = 13
     * 
     * AFTER EXAMPLE:
     * Start: row=4, col=0, matrix[4][0]=18
     * 18>13? Yes, row-- → row=3, matrix[3][0]=10
     * 10<13? Yes, col++ → col=1, matrix[3][1]=13
     * 13==13? Yes, return true ✓
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        // PURPOSE: Handle edge case - empty matrix
        if(matrix == null || matrix.length == 0) {
            return false;
        }
        
        // PURPOSE: Initialize pointers to bottom-left corner
        // Starting from bottom-left ensures we can eliminate rows/columns efficiently
        int row = matrix.length - 1;        // Start at last row (bottom)
        int col = 0;                        // Start at first column (left)
        
        // PURPOSE: Traverse the matrix using zigzag pattern
        // Continue while row is valid (>= 0) and column is valid (< number of columns)
        while(row >= 0 && col < matrix[0].length) {
            // PURPOSE: Check if current element matches target
            if(matrix[row][col] == target) {
                return true;  // Target found!
            }
            // PURPOSE: If current element is greater than target
            // Eliminate current row because all values below are even larger
            else if(matrix[row][col] > target) {
                row--;  // Move up to check smaller values
            }
            // PURPOSE: If current element is less than target
            // Eliminate current column because all values to the left are even smaller
            else {
                col++;  // Move right to check larger values
            }
        }
        
        // PURPOSE: Return false if target not found after checking all valid positions
        return false;
    }
    
    /**
     * DRIVER METHOD: Main method to demonstrate the solution with hardcoded test cases
     * Tests various scenarios including edge cases and typical cases
     */
    public static void main(String[] args) {
        Leetcode_240_Search_2D_matrix solution = new Leetcode_240_Search_2D_matrix();
        
        System.out.println("========== LeetCode 240: Search a 2D Matrix II ==========\n");
        
        // ====== TEST CASE 1: Target Found in Middle ======
        System.out.println("TEST CASE 1: Target found in middle of matrix");
        int[][] matrix1 = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        int target1 = 13;
        System.out.println("Input Matrix:");
        printMatrix(matrix1);
        System.out.println("Target: " + target1);
        boolean result1 = solution.searchMatrix(matrix1, target1);
        System.out.println("Output: " + result1);
        System.out.println("Expected: true");
        System.out.println("Explanation: 13 is found at position [3][1]\n");
        
        // ====== TEST CASE 2: Target at Corner ======
        System.out.println("TEST CASE 2: Target at bottom-right corner");
        int[][] matrix2 = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        int target2 = 30;
        System.out.println("Input Matrix:");
        printMatrix(matrix2);
        System.out.println("Target: " + target2);
        boolean result2 = solution.searchMatrix(matrix2, target2);
        System.out.println("Output: " + result2);
        System.out.println("Expected: true");
        System.out.println("Explanation: 30 is the maximum element at [4][4]\n");
        
        // ====== TEST CASE 3: Target Not Found ======
        System.out.println("TEST CASE 3: Target not present in matrix");
        int[][] matrix3 = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        int target3 = 50;
        System.out.println("Input Matrix:");
        printMatrix(matrix3);
        System.out.println("Target: " + target3);
        boolean result3 = solution.searchMatrix(matrix3, target3);
        System.out.println("Output: " + result3);
        System.out.println("Expected: false");
        System.out.println("Explanation: 50 is greater than all elements in the matrix\n");
        
        // ====== TEST CASE 4: Target at Start (Top-Left) ======
        System.out.println("TEST CASE 4: Target at top-left corner");
        int[][] matrix4 = {
            {1, 4, 7, 11, 15},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        int target4 = 1;
        System.out.println("Input Matrix:");
        printMatrix(matrix4);
        System.out.println("Target: " + target4);
        boolean result4 = solution.searchMatrix(matrix4, target4);
        System.out.println("Output: " + result4);
        System.out.println("Expected: true");
        System.out.println("Explanation: 1 is the minimum element at [0][0]\n");
        
        // ====== TEST CASE 5: Single Row Matrix ======
        System.out.println("TEST CASE 5: Single row matrix");
        int[][] matrix5 = {
            {1, 2, 3, 4, 5}
        };
        int target5 = 3;
        System.out.println("Input Matrix:");
        printMatrix(matrix5);
        System.out.println("Target: " + target5);
        boolean result5 = solution.searchMatrix(matrix5, target5);
        System.out.println("Output: " + result5);
        System.out.println("Expected: true");
        System.out.println("Explanation: 3 is found at position [0][2]\n");
        
        // ====== TEST CASE 6: Single Column Matrix ======
        System.out.println("TEST CASE 6: Single column matrix");
        int[][] matrix6 = {
            {1},
            {2},
            {3},
            {4},
            {5}
        };
        int target6 = 4;
        System.out.println("Input Matrix:");
        printMatrix(matrix6);
        System.out.println("Target: " + target6);
        boolean result6 = solution.searchMatrix(matrix6, target6);
        System.out.println("Output: " + result6);
        System.out.println("Expected: true");
        System.out.println("Explanation: 4 is found at position [3][0]\n");
        
        // ====== SUMMARY ======
        System.out.println("========== TEST SUMMARY ==========");
        System.out.println("Test Case 1 (Middle): " + (result1 == true ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 2 (Corner): " + (result2 == true ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 3 (Not Found): " + (result3 == false ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 4 (Top-Left): " + (result4 == true ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 5 (Single Row): " + (result5 == true ? "✓ PASS" : "✗ FAIL"));
        System.out.println("Test Case 6 (Single Column): " + (result6 == true ? "✓ PASS" : "✗ FAIL"));
    }
    
    /**
     * HELPER METHOD: Print matrix in readable format
     * PURPOSE: Display 2D matrix in a structured way for debugging and visualization
     */
    private static void printMatrix(int[][] matrix) {
        // PURPOSE: Iterate through each row
        for(int[] row : matrix) {
            System.out.print("[");
            // PURPOSE: Print each element in the row
            for(int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                // PURPOSE: Add comma separator between elements (except last)
                if(i < row.length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }
}
