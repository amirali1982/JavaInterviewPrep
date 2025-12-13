package com.interview.structures;

import java.util.*;

/**
 * Graph implementation using Adjacency List.
 * Demonstrates: Graph representation, BFS, DFS.
 * 
 * @param <T> Node type
 */
public class Graph<T> {
    // Adjacency List
    private final Map<T, List<T>> adjVertices = new HashMap<>();

    public void addVertex(T label) {
        adjVertices.putIfAbsent(label, new ArrayList<>());
    }

    public void addEdge(T src, T dest) {
        adjVertices.putIfAbsent(src, new ArrayList<>());
        adjVertices.putIfAbsent(dest, new ArrayList<>());
        adjVertices.get(src).add(dest);
        adjVertices.get(dest).add(src); // Undirected graph
    }

    // Breadth-First Search (Queue based)
    // Good for: Shortest path in unweighted graph
    public Set<T> breadthFirstTraversal(T root) {
        Set<T> visited = new LinkedHashSet<>();
        Queue<T> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            T vertex = queue.poll();
            for (T v : adjVertices.get(vertex)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
        return visited;
    }

    // Depth-First Search (Recursion/Stack based)
    // Good for: Path finding, topology check
    public Set<T> depthFirstTraversal(T root) {
        Set<T> visited = new LinkedHashSet<>();
        dfsRecursive(root, visited);
        return visited;
    }

    private void dfsRecursive(T vertex, Set<T> visited) {
        visited.add(vertex);
        for (T v : adjVertices.get(vertex)) {
            if (!visited.contains(v)) {
                dfsRecursive(v, visited);
            }
        }
    }
}
