package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortDemo {
    public void mergeSort(int[] arr, int low, int high) {
        int mid = low+(high-low)/2;
        if(low>=high) return;
        mergeSort(arr,low,mid);
        mergeSort(arr,mid+1,high);
        merge(arr,low, mid, high);
    }

    public void merge(int[] arr, int low, int mid, int high) {
        List<Integer> ans = new ArrayList<Integer>();
        int left = low;
        int right = mid+1;
        while(left<=mid && right<=high) {
            if(arr[left]<arr[right]) {
                ans.add(arr[left]);
                left++;
            }
            else{
                ans.add(arr[right]);
                right++;
            }
        }
        while(left<=mid) {
            ans.add(arr[left]);
            left++;
        }
        while(right<=high) {
            ans.add(arr[right]);
            right++;
        }
        for(int i=low;i<=high;i++) {
            arr[i]=ans.get(i-low);
        }
    }

    public static void main(String[] args) {
        MergeSortDemo demo = new MergeSortDemo();
        int[] arr = new int[]{42, 17, 8, 99, 23, 5, 61, 73, 15, 2};
        demo.mergeSort(arr,0,9);
        System.out.println(Arrays.toString(arr));
    }
}
