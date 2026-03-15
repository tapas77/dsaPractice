package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Course_Schedule2_leetcode_210 {
    /**
     * Finds an order in which courses can be taken given prerequisites.
     *
     * @param numCourses      Total number of courses (labeled from 0 to numCourses-1)
     * @param prerequisites  Array of prerequisite pairs [a, b] meaning to take course a you must first take b
     * @return               Array representing a valid order to take all courses, or empty array if impossible
     *
     * Example (Before):
     *   numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
     *   Output: [0,1,2,3] or [0,2,1,3]
     *
     * Example (After):
     *   numCourses = 2, prerequisites = [[1,0]]
     *   Output: [0,1]
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // Build adjacency list for each course
        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0;i<numCourses;i++){
            adj.add(new ArrayList<>());
        }

        // Track number of prerequisites (in-degree) for each course
        int[] inDegree = new int[numCourses];

        // Fill adjacency list and in-degree array
        for(int[] edge : prerequisites){
            int course = edge[0];
            int prereq = edge[1];
            adj.get(prereq).add(course); // prereq -> course
            inDegree[course]++;
        }

        // Queue for courses with no prerequisites
        Queue<Integer> q = new LinkedList<>();
        for(int i=0;i<numCourses;i++){
            if(inDegree[i]==0) q.offer(i);
        }
        int idx=0;
        int[] order = new int[numCourses];

        // Process courses in topological order
        while(!q.isEmpty()){
            int cur = q.poll();
            order[idx++]=cur;

            // Decrease in-degree for neighbors; add to queue if in-degree becomes 0
            for(int neighbour : adj.get(cur)){
                inDegree[neighbour]--;
                if(inDegree[neighbour]==0) q.offer(neighbour);
            }
        }

        // If all courses are processed, return order; else, return empty array
        return idx == numCourses ? order : new int[0];
    }

    /**
     * Main method to test findOrder with hardcoded test cases.
     */
    public static void main(String[] args) {
        Course_Schedule2_leetcode_210 solver = new Course_Schedule2_leetcode_210();

        // Test case 1: Example from Leetcode
        int numCourses1 = 4;
        int[][] prerequisites1 = {{1,0},{2,0},{3,1},{3,2}};
        int[] result1 = solver.findOrder(numCourses1, prerequisites1);
        System.out.print("Test 1 (Expected: [0,1,2,3] or [0,2,1,3]): ");
        printArray(result1);

        // Test case 2: Simple chain
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1,0}};
        int[] result2 = solver.findOrder(numCourses2, prerequisites2);
        System.out.print("Test 2 (Expected: [0,1]): ");
        printArray(result2);

        // Test case 3: Impossible case (cycle)
        int numCourses3 = 2;
        int[][] prerequisites3 = {{1,0},{0,1}};
        int[] result3 = solver.findOrder(numCourses3, prerequisites3);
        System.out.print("Test 3 (Expected: []): ");
        printArray(result3);

        // Test case 4: No prerequisites
        int numCourses4 = 3;
        int[][] prerequisites4 = {};
        int[] result4 = solver.findOrder(numCourses4, prerequisites4);
        System.out.print("Test 4 (Expected: [0,1,2] in any order): ");
        printArray(result4);
    }

    // Helper method to print arrays
    private static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(",");
        }
        System.out.println("]");
    }
}
