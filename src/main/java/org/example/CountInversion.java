package org.example;

import java.util.ArrayList;
import java.util.List;

public class CountInversion {
    public static void main(String[] args) {
        CountInversion ci = new CountInversion();
        int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println(ci.countInversions(arr,0,8));

    }
    public int countInversions(int[] arr, int low, int high) {

        int cnt=0;
        if(low>=high) return 0;
        int mid = low + (high-low)/2;
        cnt+=countInversions(arr,low,mid);
        cnt+=countInversions(arr,mid+1,high);
        cnt+=merge(arr, low, mid, high);
        return cnt;
    }

    public int merge(int[] arr, int low, int mid, int high) {
        List<Integer> ans = new ArrayList<Integer>();
        int cnt = 0;
        int left = low;
        int right = mid + 1;
        while(left<=mid && right<=high){
            if(arr[left]<=arr[right]){
                ans.add(arr[left]);
                left++;
            }
            else{
                ans.add(arr[right]);
                cnt+=mid-left+1;
                right++;
            }
        }
        while(left<=mid){
            ans.add(arr[left]);
            left++;
        }
        while(right<=high){
            ans.add(arr[right]);
            right++;
        }
        for(int i=low;i<=high;i++){
            arr[i] = ans.get(i-low);
        }
        return cnt;
    }
}
