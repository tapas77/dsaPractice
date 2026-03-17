package org.example;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Leetcode 785: Bipartite Graph Checker
 * This class provides two approaches to check if a given undirected graph is bipartite:
 * 1. BFS (Breadth-First Search)
 * 2. DFS (Depth-First Search)
 *
 * A bipartite graph can be colored using two colors such that no two adjacent nodes have the same color.
 */
public class Bipartite_graph_Leetcode_785 {
    /**
     * Approach 1: BFS
     * Checks if the graph is bipartite using Breadth-First Search.
     * Each node is colored with 1 or -1. If two adjacent nodes have the same color, the graph is not bipartite.
     *
     * Example:
     * Input: [[1,3],[0,2],[1,3],[0,2]]
     * Before: All nodes uncolored (color = 0)
     * After: All nodes colored with 1 or -1, no adjacent nodes have same color => bipartite
     * Returns: true
     */
    public boolean isBipartiteBFS(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n]; // 0: uncolored, 1: color1, -1: color2

        for(int i=0;i<n;i++){
            if(color[i]!=0) continue; // Skip already colored nodes

            Queue<Integer> q = new LinkedList<>();
            q.offer(i);
            color[i]=1;

            while(!q.isEmpty()){
                int node = q.poll();
                for(int nei : graph[node]){
                    if(color[nei]==0){
                        color[nei]=-color[node]; // Color neighbor with opposite color
                        q.offer(nei);
                    }
                    else if(color[nei]==color[node]) return false; // Same color found, not bipartite
                }
            }
        }
        return true;
    }

    /**
     * Approach 2: DFS
     * Checks if the graph is bipartite using Depth-First Search.
     * Recursively colors nodes and checks for conflicts.
     *
     * Example:
     * Input: [[1,3],[0,2],[1,3],[0,2]]
     * Before: All nodes uncolored (color = 0)
     * After: All nodes colored with 1 or -1, no adjacent nodes have same color => bipartite
     * Returns: true
     */
    public boolean isBipartiteDFS(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n]; // 0: uncolored, 1: color1, -1: color2
        for(int i=0;i<n;i++){
            if(color[i]==0 && !dfs(i,1,graph,color)) return false;
        }
        return true;
    }

    /**
     * Helper method for DFS coloring.
     * @param node Current node
     * @param c1 Color to assign
     * @param graph Adjacency list
     * @param color Color array
     * @return true if bipartite, false otherwise
     */
    private boolean dfs(int node, int c1, int[][] graph, int[] color){
        color[node] = c1;
        for(int nei : graph[node]){
            if(color[nei]==0 && !dfs(nei, -c1, graph, color)) return false;
            else if(color[nei]==c1) return false; // Conflict found
        }
        return true;
    }
    /**
     * Main method to test both approaches with hardcoded test cases.
     * Demonstrates before and after for each example.
     */
    public static void main(String[] args) {
        Bipartite_graph_Leetcode_785 solver = new Bipartite_graph_Leetcode_785();

        // Example 1: Bipartite graph
        int[][] graph1 = {
            {1,3}, // Node 0 connected to 1,3
            {0,2}, // Node 1 connected to 0,2
            {1,3}, // Node 2 connected to 1,3
            {0,2}  // Node 3 connected to 0,2
        };
        // Before: All nodes uncolored
        // After: All nodes colored with 1 or -1, no adjacent nodes have same color
        System.out.println("BFS Example 1 (should be true): " + solver.isBipartiteBFS(graph1));
        System.out.println("DFS Example 1 (should be true): " + solver.isBipartiteDFS(graph1));

        // Example 2: Not bipartite (odd cycle)
        int[][] graph2 = {
            {1,2,3}, // Node 0 connected to 1,2,3
            {0,2},   // Node 1 connected to 0,2
            {0,1,3}, // Node 2 connected to 0,1,3
            {0,2}    // Node 3 connected to 0,2
        };
        // Before: All nodes uncolored
        // After: Conflict found, adjacent nodes have same color
        System.out.println("BFS Example 2 (should be false): " + solver.isBipartiteBFS(graph2));
        System.out.println("DFS Example 2 (should be false): " + solver.isBipartiteDFS(graph2));

        // Example 3: Disconnected graph, bipartite
        int[][] graph3 = {
            {1},    // Node 0 connected to 1
            {0},    // Node 1 connected to 0
            {},     // Node 2 isolated
            {4},    // Node 3 connected to 4
            {3}     // Node 4 connected to 3
        };
        System.out.println("BFS Example 3 (should be true): " + solver.isBipartiteBFS(graph3));
        System.out.println("DFS Example 3 (should be true): " + solver.isBipartiteDFS(graph3));
    }
}
