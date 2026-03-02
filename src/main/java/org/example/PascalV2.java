package org.example;

public class PascalV2 {
    public void printNthRow(int n){
        int ans = 1;
        System.out.print(ans);
        for(int i=1;i<n;i++){
            ans = ans * (n-i);
            ans = ans / (i);
            System.out.print(" " +ans);
        }


    }
    public static void main(String[] args) {
        PascalV2 p = new PascalV2();
        p.printNthRow(6);

    }
}
