package org.example;

import java.util.Stack;

public class Calculator2 {

    public int calculate(String s) {
        long n = 0;
        char sgn = '+';

        Stack<Integer> st = new Stack<>();
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if(Character.isDigit(c)) n = n*10+(c-'0');
            if((!Character.isDigit(c) && !Character.isWhitespace(c)) || i==s.length()-1){
                if(sgn=='+') st.push((int)n);
                if(sgn=='-') st.push((int)-n);
                if(sgn=='*') st.push(st.pop()*(int)n);
                if(sgn=='/') st.push(st.pop()/(int)n);

                sgn=c;
                n=0L;
            }
        }
        int res = 0;
        while(!st.isEmpty()){
            res+=st.pop();
        }
        return res;
    }

    public static void main(String[] args) {
        Calculator2 calc = new Calculator2();

        // Test case 1: "3+2*2" should return 7
        String input1 = "3+2*2";
        System.out.println("Input: " + input1 + " => Output: " + calc.calculate(input1));

        // Test case 2: "6/2" should return 3
        String input2 = "6/2";
        System.out.println("Input: " + input2 + " => Output: " + calc.calculate(input2));

        // Test case 3: " 2-1 + 2 " should return 3
        String input3 = " 2-1 + 2 ";
        System.out.println("Input: " + input3 + " => Output: " + calc.calculate(input3));
    }
}

