package org.example;

import java.util.*;

public class Course_Schedule2_leetcode_210 {
    /**
     * Finds an order in which courses can be taken given prerequisites using BFS (Kahn's Algorithm).
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
     * Finds an order in which courses can be taken given prerequisites using DFS (cycle detection and topological sort).
     *
     * @param numCourses      Total number of courses (labeled from 0 to numCourses-1)
     * @param prerequisites  Array of prerequisite pairs [a, b] meaning to take course a you must first take b
     * @return               Array representing a valid order to take all courses, or empty array if impossible
     *
     * Example (Before):
     *   numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
     *   Output: [0,2,1,3] or [0,1,2,3]
     *
     * Example (After):
     *   numCourses = 2, prerequisites = [[1,0]]
     *   Output: [0,1]
     */
    public int[] findOrderDfs(int numCourses, int[][] prerequisites) {
        // Build adjacency list for each course
        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0;i<numCourses;i++){
            adj.add(new ArrayList<>());
        }

        // Fill adjacency list
        for(int[] p : prerequisites){
            int course = p[0];
            int prereq = p[1];
            adj.get(prereq).add(course);
        }
        // 0 = unvisited, 1 = visiting, 2 = visited
        int[] state = new int[numCourses];
        List<Integer> order = new ArrayList<>();

        // Run DFS for each course
        for(int i=0;i<numCourses;i++){
            if(state[i]==0 && dfs(i, adj, state, order)){
                // Cycle detected, impossible to finish all courses
                return new int[0];
            }
        }
        // Reverse order to get correct topological sort
        Collections.reverse(order);
        return order.stream().mapToInt(i->i).toArray();
    }

    /**
     * Helper DFS method for cycle detection and topological sort.
     * @param i      Current course
     * @param adj    Adjacency list
     * @param state  State array (0=unvisited, 1=visiting, 2=visited)
     * @param order  List to store course order
     * @return       True if a cycle is detected, false otherwise
     */
    private boolean dfs(int i, List<List<Integer>> adj, int[] state, List<Integer> order){
        state[i]=1; // Mark as visiting
        for(int v : adj.get(i)){
            if(state[v]==1) return true; // Cycle detected
            if(state[v]==0 && dfs(v,adj,state,order)) return true;
        }
        state[i]=2; // Mark as visited
        order.add(i);
        return false;
    }

    /**
     * Main method to test both findOrder (BFS) and findOrderDfs (DFS) with hardcoded test cases.
     * Includes before/after examples in comments.
     */
    public static void main(String[] args) {
        Course_Schedule2_leetcode_210 solver = new Course_Schedule2_leetcode_210();

        // Test case 1: Example from Leetcode
        // Before: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
        // After: Output: [0,1,2,3] or [0,2,1,3]
        int numCourses1 = 4;
        int[][] prerequisites1 = {{1,0},{2,0},{3,1},{3,2}};
        int[] result1_bfs = solver.findOrder(numCourses1, prerequisites1);
        int[] result1_dfs = solver.findOrderDfs(numCourses1, prerequisites1);
        System.out.print("BFS Test 1 (Expected: [0,1,2,3] or [0,2,1,3]): ");
        printArray(result1_bfs);
        System.out.print("DFS Test 1 (Expected: [0,1,2,3] or [0,2,1,3]): ");
        printArray(result1_dfs);

        // Test case 2: Simple chain
        // Before: numCourses = 2, prerequisites = [[1,0]]
        // After: Output: [0,1]
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1,0}};
        int[] result2_bfs = solver.findOrder(numCourses2, prerequisites2);
        int[] result2_dfs = solver.findOrderDfs(numCourses2, prerequisites2);
        System.out.print("BFS Test 2 (Expected: [0,1]): ");
        printArray(result2_bfs);
        System.out.print("DFS Test 2 (Expected: [0,1]): ");
        printArray(result2_dfs);

        // Test case 3: Impossible case (cycle)
        // Before: numCourses = 2, prerequisites = [[1,0],[0,1]]
        // After: Output: []
        int numCourses3 = 2;
        int[][] prerequisites3 = {{1,0},{0,1}};
        int[] result3_bfs = solver.findOrder(numCourses3, prerequisites3);
        int[] result3_dfs = solver.findOrderDfs(numCourses3, prerequisites3);
        System.out.print("BFS Test 3 (Expected: []): ");
        printArray(result3_bfs);
        System.out.print("DFS Test 3 (Expected: []): ");
        printArray(result3_dfs);

        // Test case 4: No prerequisites
        // Before: numCourses = 3, prerequisites = []
        // After: Output: [0,1,2] in any order
        int numCourses4 = 3;
        int[][] prerequisites4 = {};
        int[] result4_bfs = solver.findOrder(numCourses4, prerequisites4);
        int[] result4_dfs = solver.findOrderDfs(numCourses4, prerequisites4);
        System.out.print("BFS Test 4 (Expected: [0,1,2] in any order): ");
        printArray(result4_bfs);
        System.out.print("DFS Test 4 (Expected: [0,1,2] in any order): ");
        printArray(result4_dfs);
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
