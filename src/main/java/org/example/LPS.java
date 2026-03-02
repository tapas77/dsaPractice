package org.example;

public class LPS {
    public String longestPalindromicSubstring(String s) {

        int n = s.length();
        String longest = "";
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                String sub = s.substring(i, j + 1);
                if (isPalindrome(sub) && sub.length() > longest.length()) {
                    longest = sub;
                }
            }
        }
        return longest;
    }

    private boolean isPalindrome (String str){
        int i = 0;
        int r = str.length()-1;
        while (i < r) {
            if (str.charAt(i++) != str.charAt(r--)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LPS lps = new LPS();
        String x = "dababc";
        System.out.println(lps.longestPalindromicSubstring(x));
    }
}