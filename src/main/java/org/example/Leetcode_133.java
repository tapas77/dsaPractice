package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;

// Definition for a Node in an undirected graph
class Node {
    public int val;
    public List<Node> neighbors;
    // Default constructor
    public Node() {
        val = 0;
        neighbors = new ArrayList<>(); // Use diamond operator
    }
    // Constructor with value
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<>(); // Use diamond operator
    }
    // Constructor with value and neighbors
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}

public class Leetcode_133 {

    // Map to keep track of already cloned nodes
    Map<Node, Node> map;

    // Function to clone the graph using DFS
    public Node cloneGraph(Node node) {
        if (node == null) return null;
        map = new HashMap<>();
        Node cloneNode = new Node(node.val);
        map.put(node, cloneNode);
        dfs(node, cloneNode, map);
        return cloneNode;
    }

    // Depth-first search to clone neighbors recursively
    public void dfs(Node node, Node cloneNode, Map<Node, Node> map) {
        for (Node neighbor : node.neighbors) {
            if (!map.containsKey(neighbor)) {
                Node cloneNeighbor = new Node(neighbor.val);
                map.put(neighbor, cloneNeighbor);
                cloneNode.neighbors.add(cloneNeighbor);
                dfs(neighbor, cloneNeighbor, map);
            } else {
                cloneNode.neighbors.add(map.get(neighbor));
            }
        }
    }

    // Helper function to print the graph (for testing)
    public void printGraph(Node node, HashSet<Node> visited) {
        if (node == null || visited.contains(node)) return;
        visited.add(node);
        System.out.print("Node " + node.val + " neighbors: ");
        for (Node neighbor : node.neighbors) {
            System.out.print(neighbor.val + " ");
        }
        System.out.println();
        for (Node neighbor : node.neighbors) {
            printGraph(neighbor, visited);
        }
    }

    // Main method with a hardcoded test case
    public static void main(String[] args) {
        // Create a simple graph: 1 -- 2 -- 3 -- 4, and 1 -- 4
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        // Defining neighbors (undirected graph)
        node1.neighbors.add(node2);
        node1.neighbors.add(node4);
        node2.neighbors.add(node1);
        node2.neighbors.add(node3);
        node3.neighbors.add(node2);
        node3.neighbors.add(node4);
        node4.neighbors.add(node1);
        node4.neighbors.add(node3);

        Leetcode_133 solution = new Leetcode_133();

        // Clone the graph
        Node clonedGraph = solution.cloneGraph(node1);

        // Print original graph
        System.out.println("Original graph:");
        solution.printGraph(node1, new java.util.HashSet<>());

        // Print cloned graph
        System.out.println("Cloned graph:");
        solution.printGraph(clonedGraph, new java.util.HashSet<>());
    }
}
