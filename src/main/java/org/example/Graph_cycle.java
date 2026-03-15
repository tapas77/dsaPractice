package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph_cycle {
    /**
     * Detects cycle in an undirected graph using DFS.
     * @param edges Edge list where each edge is represented as [u, v] (1-based indexing)
     * @param n Number of nodes
     * @param m Number of edges
     * @return "Yes" if a cycle exists, otherwise "No"
     */
    public static String cycleDetection(int[][] edges, int n, int m) {
        // Create adjacency list for the graph
        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0;i<n;i++){
            adj.add(new ArrayList<>());
        }
        // Build the adjacency list from the edge list
        for(int[] arr : edges){
            adj.get(arr[0]-1).add(arr[1]-1); // Convert 1-based to 0-based index
            adj.get(arr[1]-1).add(arr[0]-1);
        }
        boolean[] visited = new boolean[n];

        // Check for cycle in each connected component
        for(int i=0;i<n;i++){
            if(!visited[i]){
                if(dfs(i,-1,adj,visited)) return "Yes";
            }
        }
        return "No";
    }
    /**
     * Helper DFS function to detect cycle in an undirected graph.
     * @param node Current node
     * @param parent Parent node in DFS tree
     * @param adj Adjacency list
     * @param visited Visited array
     * @return true if a cycle is found, false otherwise
     */
    private static boolean dfs(int node, int parent, List<List<Integer>> adj, boolean[] visited){
        visited[node]=true;
        for(int x : adj.get(node)){
            if(x==parent) continue; // Skip the parent node
            if(visited[x]) return true; // Found a back edge, cycle exists
            if(dfs(x,node,adj,visited)) return true;
        }
        return false;
    }

    /**
     * Detects cycle in an undirected graph using BFS.
     * @param edges Edge list where each edge is represented as [u, v] (1-based indexing)
     * @param n Number of nodes
     * @param m Number of edges
     * @return "Yes" if a cycle exists, otherwise "No"
     */
    public static String cycleDetection2(int[][] edges, int n, int m) {
        // Create adjacency list for the graph
        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0;i<n;i++){
            adj.add(new ArrayList<>());
        }
        // Build the adjacency list from the edge list
        for(int[] arr : edges){
            adj.get(arr[0]-1).add(arr[1]-1); // Convert 1-based to 0-based index
            adj.get(arr[1]-1).add(arr[0]-1);
        }
        boolean[] visited = new boolean[n];

        // Check for cycle in each connected component
        for(int i=0;i<n;i++){
            if(!visited[i]){
                if(bfs(i, adj,visited)) return "Yes";
            }
        }
        return "No";
    }
    /**
     * Helper BFS function to detect cycle in an undirected graph.
     * @param node Starting node
     * @param adj Adjacency list
     * @param visited Visited array
     * @return true if a cycle is found, false otherwise
     */
    private static boolean bfs(int node, List<List<Integer>> adj, boolean[] visited){
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{node,-1}); // Each element is [currentNode, parentNode]
        visited[node]=true;
        while(!q.isEmpty()){
            int[] cur = q.poll();
            int curNode = cur[0];
            int parentNode = cur[1];
            for(int neigh : adj.get(curNode)){
                if(!visited[neigh]){
                    visited[neigh]=true;
                    q.offer(new int[]{neigh,curNode});
                }
                else if(neigh!=parentNode) return true; // Found a back edge
            }
        }
        return false;
    }

    /**
     * Detects cycle in an undirected graph using Disjoint Set Union (Union-Find).
     * @param edges Edge list where each edge is represented as [u, v] (1-based indexing)
     * @param n Number of nodes
     * @param m Number of edges
     * @return "Yes" if a cycle exists, otherwise "No"
     */
    public static String cycleDetection3(int[][] edges, int n, int m) {
        int[] parent = new int[n];
        int[] rank = new int[n];

        // Initialize parent and rank arrays
        for(int i=0;i<n;i++) parent[i]=i;

        // Iterate through all edges
        for(int[] e : edges){
            int u = e[0]-1;
            int v = e[1]-1;

            int pu = find(parent, u);
            int pv = find(parent, v);

            if(pu == pv) return "Yes"; // cycle found

            union(parent, rank, pu, pv);
        }

        return "No";
    }
    /**
     * Find operation with path compression for DSU.
     */
    private static int find(int[] parent, int x){
        if(parent[x]!=x)
            parent[x] = find(parent, parent[x]);
        return parent[x];
    }
    /**
     * Union operation by rank for DSU.
     */
    private static void union(int[] parent, int[] rank, int x, int y) {
        if(rank[x] < rank[y]) parent[x] = y;
        else if (rank[x]>rank[y]) parent[y] = x;
        else{
            parent[y] = x;
            rank[x]++;
        }
    }

    /**
     * Main method to test all three cycle detection approaches with hardcoded testcases.
     */
    public static void main(String[] args) {
        // Example 1: Graph with a cycle
        // Graph: 1-2-3-1 (cycle)
        int n1 = 3; // number of nodes
        int m1 = 3; // number of edges
        int[][] edges1 = { {1,2}, {2,3}, {3,1} };
        System.out.println("Testcase 1 (Should be Yes):");
        System.out.println("DFS: " + cycleDetection(edges1, n1, m1));
        System.out.println("BFS: " + cycleDetection2(edges1, n1, m1));
        System.out.println("Union-Find: " + cycleDetection3(edges1, n1, m1));
        System.out.println();

        // Example 2: Graph without a cycle
        // Graph: 1-2, 2-3 (no cycle)
        int n2 = 3;
        int m2 = 2;
        int[][] edges2 = { {1,2}, {2,3} };
        System.out.println("Testcase 2 (Should be No):");
        System.out.println("DFS: " + cycleDetection(edges2, n2, m2));
        System.out.println("BFS: " + cycleDetection2(edges2, n2, m2));
        System.out.println("Union-Find: " + cycleDetection3(edges2, n2, m2));
        System.out.println();

        // Example 3: Disconnected graph, one component has a cycle
        // Graph: 1-2-3-1 and 4-5 (no cycle in second component)
        int n3 = 5;
        int m3 = 4;
        int[][] edges3 = { {1,2}, {2,3}, {3,1}, {4,5} };
        System.out.println("Testcase 3 (Should be Yes):");
        System.out.println("DFS: " + cycleDetection(edges3, n3, m3));
        System.out.println("BFS: " + cycleDetection2(edges3, n3, m3));
        System.out.println("Union-Find: " + cycleDetection3(edges3, n3, m3));
        System.out.println();

        // Example 4: Disconnected graph, no cycles
        // Graph: 1-2, 3-4
        int n4 = 4;
        int m4 = 2;
        int[][] edges4 = { {1,2}, {3,4} };
        System.out.println("Testcase 4 (Should be No):");
        System.out.println("DFS: " + cycleDetection(edges4, n4, m4));
        System.out.println("BFS: " + cycleDetection2(edges4, n4, m4));
        System.out.println("Union-Find: " + cycleDetection3(edges4, n4, m4));
        System.out.println();
    }
}