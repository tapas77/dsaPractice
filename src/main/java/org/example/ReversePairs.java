package org.example;

import java.util.ArrayList;
import java.util.List;

public class ReversePairs {
    public static void main(String[] args) {
        int[] a = {2147483647,2147483647,2147483647,2147483647,2147483647,2147483647};
        ReversePairs rp = new ReversePairs();
        int cnt = rp.countInversions(a, 0, a.length-1);
        System.out.println("The number of reverse pair is: " + cnt);
    }

    public int countInversions(int[] arr, int low, int high) {
        int cnt=0;
        if(low>=high) return cnt;
        int mid = low + (high-low)/2;
        cnt+=countInversions(arr,low,mid);
        cnt+=countInversions(arr,mid+1,high);
        cnt+=countReversePairs(arr, low, mid, high);
        merge(arr, low, mid, high);
        return cnt;
    }
    public int countReversePairs(int[] arr, int low, int mid, int high) {
        int cnt=0;
        int right=mid+1;
        for(int i=low;i<=mid;i++) {
            while(right<=high && (long)arr[i]>2L*arr[right]){
                right++;
            }
            cnt+=right-(mid+1);
        }
        return cnt;
    }

    public void merge(int[] arr, int low, int mid, int high) {
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
    }
}
