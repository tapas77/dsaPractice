package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Leetcode 207: Course Schedule
 * Determines if you can finish all courses given prerequisites.
 * Uses Kahn's Algorithm (BFS Topological Sort).
 *
 * Example:
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Before: Course 1 depends on 0. Graph: 0 -> 1
 * After: Can finish all courses: true
 *
 * Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
 * Before: 0 <-> 1 (cycle)
 * After: Can finish all courses: false
 */
public class CourseSchedule_Leetcode_207 {

    /**
     * Determines if all courses can be finished given prerequisites.
     * @param numCourses Number of courses
     * @param prerequisites Array of prerequisite pairs [a, b] (to take a, must take b first)
     * @return true if possible to finish all courses, false otherwise
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Build adjacency list for the graph
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        // inDegree[i] = number of prerequisites for course i
        int[] inDegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            // pre[1] -> pre[0] (to take pre[0], need pre[1])
            adj.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }

        // Queue for BFS: start with courses having no prerequisites
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) q.offer(i);
        }

        int cnt = 0; // Count of courses that can be taken
        while (!q.isEmpty()) {
            int node = q.poll();
            cnt++;
            // For each course dependent on 'node', reduce its in-degree
            for (int next : adj.get(node)) {
                inDegree[next]--;
                // If in-degree becomes 0, add to queue
                if (inDegree[next] == 0) q.offer(next);
            }
        }

        // If all courses are taken, return true
        return cnt == numCourses;
    }

    /**
     * Main method with hardcoded test cases.
     */
    public static void main(String[] args) {
        CourseSchedule_Leetcode_207 solver = new CourseSchedule_Leetcode_207();

        // Test case 1: Simple acyclic
        int numCourses1 = 2;
        int[][] prerequisites1 = {{1, 0}};
        // Before: 0 -> 1
        // After: Can finish all courses: true
        System.out.println("Test 1: " + solver.canFinish(numCourses1, prerequisites1)); // true

        // Test case 2: Simple cycle
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1, 0}, {0, 1}};
        // Before: 0 <-> 1 (cycle)
        // After: Can finish all courses: false
        System.out.println("Test 2: " + solver.canFinish(numCourses2, prerequisites2)); // false

        // Test case 3: No prerequisites
        int numCourses3 = 3;
        int[][] prerequisites3 = {};
        // Before: No edges
        // After: Can finish all courses: true
        System.out.println("Test 3: " + solver.canFinish(numCourses3, prerequisites3)); // true

        // Test case 4: Multiple dependencies, no cycle
        int numCourses4 = 4;
        int[][] prerequisites4 = {{1, 0}, {2, 1}, {3, 2}};
        // Before: 0 -> 1 -> 2 -> 3
        // After: Can finish all courses: true
        System.out.println("Test 4: " + solver.canFinish(numCourses4, prerequisites4)); // true

        // Test case 5: Multiple dependencies, with cycle
        int numCourses5 = 4;
        int[][] prerequisites5 = {{1, 0}, {2, 1}, {3, 2}, {1, 3}};
        // Before: 0 -> 1 -> 2 -> 3 -> 1 (cycle)
        // After: Can finish all courses: false
        System.out.println("Test 5: " + solver.canFinish(numCourses5, prerequisites5)); // false
    }
}
