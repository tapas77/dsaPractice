package org.example;

import javax.swing.*;
import java.util.*;

public class Password {

    public static void main(String[] args) {
        Scanner sc=  new Scanner(System.in);
        String word = "abcd";
        System.out.println(countMinimumOperations(word));
    }

    public static int countMinimumOperations(String passKey){
        HashSet<Character> hs = new HashSet<>();
        hs.add('a');
        hs.add('e');
        hs.add('i');
        hs.add('o');
        hs.add('u');
        HashMap<Character,Integer> hm = new HashMap<>();
        PriorityQueue<Pair> pq = new PriorityQueue<>((a,b)->a.x-b.x);
        int consonants=0;
        int vowels=0;
        for(int i=0;i<passKey.length();i++){
            if(hs.contains(passKey.charAt(i)))vowels++;
            else consonants++;
            hm.put(passKey.charAt(i),hm.getOrDefault(passKey.charAt(i),0)+1);
        }

        if(vowels==consonants) return 0;
        int ans=0;
        if(vowels>consonants) return (vowels-consonants)/2;
        else{
            int diff = (consonants-vowels)/2;
            int[] freq = new int[26];
            freq[0]=0;
            char[] check = new char[]{'a','e','i','o','u'};
            for(int i=0;i<26;i++){                freq[i]=1000;
                char x = (char)('a'+i);
                for(int j=0;j<5;j++){
                    freq[i] = Math.min(freq[i],Math.abs(x-check[j]));
                }
            }
            for(int i=0;i<26;i++){
                pq.add(new Pair(freq[i],(char)('a'+i)));
            }
            int g=5;
            while(g!=0){
                pq.remove();
                g--;
            }
            while(diff>0){
                Pair f = pq.remove();
                if(hm.containsKey((f.c))){
                    int i=f.c-'a';
                    if(hm.get(f.c)>diff){
                        ans+=diff*freq[i];
                        return ans;
                    }
                    else{
                        ans+=hm.get(f.c)*freq[i];
                        diff-=hm.get(f.c);
                    }
                }
            }
        }
        return ans;
    }
}
class Pair{
    int x;
    char c;

    Pair(int x,char c){
        this.c=c;
        this.x = x;
    }
}
