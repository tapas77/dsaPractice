package org.example;

import java.util.Stack;

public class MaxStack {
    Stack<Integer> st1 = new Stack<>();
    Stack<Integer> st2 = new Stack<>();

    public MaxStack(){
        st1 = new Stack<>();
        st2 = new Stack<>();
    }

    public void push(int value){
        st1.push(value);
        if(st2.isEmpty()){
            st2.push(value);
        }
        else{
            st2.push(Math.max(value,st2.peek()));
        }
    }
    public int pop(){
        st1.pop();
        st2.pop();
        return st1.pop();
    }
    public int getMax(){
        return st2.peek();
    }

    public void printStack(Stack<Integer> stack) {
        for(int i=0;i<stack.size();i++){
            System.out.print(stack.get(i)+" ");
        }
    }

    public static void main(String[] args) {
        MaxStack ms = new MaxStack();
        ms.push(3);
        ms.push(7);
        ms.push(2);
        ms.push(9);

//        System.out.println(ms.st1);
//        System.out.println(ms.getMax()); //10

        ms.printStack(ms.st1);

//        ms.pop();
//        System.out.println(ms.st1);

//        System.out.println(ms.getMax()); //7
//
//        ms.pop();
//        System.out.println(ms.getMax());

//        ms.pop();
//        System.out.println(ms.st1);





    }
}
