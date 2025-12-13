package com.interview.structures;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class AdvancedTests {

    @Test
    void testBinarySearchTree() {
        BinarySearchTree bst = new BinarySearchTree();
        bst.insert(50);
        bst.insert(30);
        bst.insert(20);
        bst.insert(40);
        bst.insert(70);
        bst.insert(60);
        bst.insert(80);

        assertTrue(bst.contains(20));
        assertTrue(bst.contains(80));
        assertFalse(bst.contains(100));

        // Manual verification of sorted property essentially done via logic check
        // Ideally we capture print output or change traverse method to return list
    }

    @Test
    void testGraphTraversal() {
        Graph<String> graph = new Graph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "D");

        Set<String> bfs = graph.breadthFirstTraversal("A");
        // A -> B, D -> C. All should be visited.
        assertEquals(4, bfs.size());
        assertTrue(bfs.contains("C"));

        Set<String> dfs = graph.depthFirstTraversal("A");
        assertEquals(4, dfs.size());
    }

    @Test
    void testTrie() {
        Trie trie = new Trie();
        trie.insert("apple");

        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app")); // 'app' is prefix, not word
        assertTrue(trie.startsWith("app"));

        trie.insert("app");
        assertTrue(trie.search("app")); // Now it is a word
    }
}
