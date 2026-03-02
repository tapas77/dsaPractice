package org.example;

/**
 * Calculator2_ConstantSpace - A string expression calculator using O(1) space complexity
 *
 * This class solves the Basic Calculator II problem efficiently with constant space.
 *
 * Problem: Evaluate a string expression containing integers and operators (+, -, *, /)
 * with respect to operator precedence (multiplication and division before addition and subtraction).
 *
 * Approach (Constant Space Solution):
 * Instead of using a stack to store intermediate results, we use two variables:
 * - 'last': Stores the result of the last operation (for * and / priority)
 * - 'res': Accumulates the final result (handles + and - operations)
 *
 * Key Insight: When we encounter an operator, we immediately resolve the operation
 * with the current number based on the previous operator's precedence.
 */
public class Calculator2_ConstantSpace {

    /**
     * Calculates the result of a mathematical expression string with proper operator precedence.
     *
     * Algorithm Explanation:
     * 1. Track the current number being formed by digits
     * 2. Track the previous operator to determine how to process the current number
     * 3. When an operator is encountered (or we reach the end of the string):
     *    - If previous operator was '+': add the last result to total, store current number as last
     *    - If previous operator was '-': add the negated last result to total, store negative current as last
     *    - If previous operator was '*': multiply last by current (no immediate addition to total)
     *    - If previous operator was '/': divide last by current (no immediate addition to total)
     * 4. Add the final 'last' value to the total result
     *
     * Time Complexity: O(n) - single pass through the string
     * Space Complexity: O(1) - only using a few variables regardless of input size
     *
     * @param s the mathematical expression string (e.g., "3+2*2", "1+2-3*4/5")
     * @return the calculated result as an integer
     */
    public int calculate(String s) {
        // 'sgn' tracks the current/previous operator: '+', '-', '*', '/'
        char sgn = '+';

        // 'last' holds the result of high-precedence operations (* and /)
        // Allows us to handle multiplication and division before addition/subtraction
        long last = 0;

        // 'current' accumulates digits to form the current number being read
        // Example: in "123+", current becomes 123
        long current = 0;

        // 'res' accumulates the final result (sum of all operands after precedence handling)
        long res = 0;

        // Iterate through each character in the expression
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // If current character is a digit, build the current number
            if (Character.isDigit(c)) {
                current = current * 10 + (c - '0');
            }

            // Process when we encounter an operator OR reach the end of string
            // (!Character.isDigit(c) && !Character.isWhitespace(c)): found an operator
            // i==s.length()-1: reached the last character (don't miss the final number)
            if ((!Character.isDigit(c) && !Character.isWhitespace(c)) || i == s.length() - 1) {

                // Handle the previous operator with the current number
                if (sgn == '+') {
                    // For addition: add the last computed value to result
                    res += last;
                    // The current number becomes the new 'last' value
                    last = current;
                }
                else if (sgn == '-') {
                    // For subtraction: add the negated last value to result
                    res += last;
                    // Store the negative of current number as the new 'last'
                    last = -current;
                }
                else if (sgn == '*') {
                    // For multiplication: multiply immediately (higher precedence)
                    // Don't add to result yet - wait for the next addition/subtraction
                    last = last * current;
                }
                else if (sgn == '/') {
                    // For division: divide immediately (higher precedence)
                    // Note: Integer division truncates towards zero
                    last = last / current;
                }

                // Update the operator for the next iteration
                sgn = c;

                // Reset current number for the next operand
                current = 0L;
            }
        }

        // Don't forget to add the last computed value (from the final operator)
        res += last;

        return (int) res;
    }

    /**
     * Main method with hardcoded test cases to demonstrate the calculator
     */
    public static void main(String[] args) {
        Calculator2_ConstantSpace calc = new Calculator2_ConstantSpace();

        System.out.println("=== Calculator2 - Constant Space Solution ===\n");

        // Test Case 1: Simple addition and multiplication with proper precedence
        // Expression: "3+2*2"
        // Step-by-step: 3 + (2*2) = 3 + 4 = 7
        String test1 = "3+2*2";
        int result1 = calc.calculate(test1);
        System.out.println("Test 1: \"" + test1 + "\"");
        System.out.println("Expected: 7 (3 + 2*2 = 3 + 4 = 7)");
        System.out.println("Result: " + result1);
        System.out.println("Status: " + (result1 == 7 ? "✓ PASS" : "✗ FAIL") + "\n");

        // Test Case 2: Division operation
        // Expression: "6/2"
        // Step-by-step: 6 / 2 = 3
        String test2 = "6/2";
        int result2 = calc.calculate(test2);
        System.out.println("Test 2: \"" + test2 + "\"");
        System.out.println("Expected: 3 (6 / 2 = 3)");
        System.out.println("Result: " + result2);
        System.out.println("Status: " + (result2 == 3 ? "✓ PASS" : "✗ FAIL") + "\n");

        // Test Case 3: Mixed operators with spaces
        // Expression: " 2-1 + 2 "
        // Step-by-step: 2 - 1 + 2 = 1 + 2 = 3
        String test3 = " 2-1 + 2 ";
        int result3 = calc.calculate(test3);
        System.out.println("Test 3: \"" + test3 + "\"");
        System.out.println("Expected: 3 (2 - 1 + 2 = 3)");
        System.out.println("Result: " + result3);
        System.out.println("Status: " + (result3 == 3 ? "✓ PASS" : "✗ FAIL") + "\n");

        // Test Case 4: Complex expression with multiple operations
        // Expression: "1+2-3*4/5"
        // Step-by-step: 1 + 2 - (3*4/5) = 1 + 2 - (12/5) = 1 + 2 - 2 = 1
        String test4 = "1+2-3*4/5";
        int result4 = calc.calculate(test4);
        System.out.println("Test 4: \"" + test4 + "\"");
        System.out.println("Expected: 1 (1 + 2 - 3*4/5 = 1 + 2 - 2 = 1)");
        System.out.println("Result: " + result4);
        System.out.println("Status: " + (result4 == 1 ? "✓ PASS" : "✗ FAIL") + "\n");

        // Test Case 5: Consecutive multiplications
        // Expression: "2*3*4"
        // Step-by-step: 2 * 3 * 4 = 6 * 4 = 24
        String test5 = "2*3*4";
        int result5 = calc.calculate(test5);
        System.out.println("Test 5: \"" + test5 + "\"");
        System.out.println("Expected: 24 (2 * 3 * 4 = 24)");
        System.out.println("Result: " + result5);
        System.out.println("Status: " + (result5 == 24 ? "✓ PASS" : "✗ FAIL") + "\n");

        // Test Case 6: Expression with leading zeros
        // Expression: "002-1"
        // Step-by-step: 2 - 1 = 1
        String test6 = "002-1";
        int result6 = calc.calculate(test6);
        System.out.println("Test 6: \"" + test6 + "\"");
        System.out.println("Expected: 1 (002 - 1 = 2 - 1 = 1)");
        System.out.println("Result: " + result6);
        System.out.println("Status: " + (result6 == 1 ? "✓ PASS" : "✗ FAIL") + "\n");
    }
}
