package com.interview.structures;

import java.util.HashMap;
import java.util.Map;

/**
 * Least Recently Used (LRU) Cache using HashMap + Doubly Linked List.
 * This effectively rebuilds LinkedHashMap behavior.
 * O(1) Get and Put.
 */
public class LRUCache<K, V> {

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node<K, V>> map;
    // Dummy head and tail for easy deletion/insertion
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        head = new Node<>(null, null);
        tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K, V> node = map.get(key);
        // Move accessed node to front (Most Recently Used)
        remove(node);
        addFirst(node);
        return node.value;
    }

    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> oldNode = map.get(key);
            remove(oldNode);
            // Key already exists, just update value and move to front
            oldNode.value = value;
            addFirst(oldNode);
        } else {
            if (map.size() >= capacity) {
                // Evict LRU (last node before tail)
                map.remove(tail.prev.key);
                remove(tail.prev);
            }
            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addFirst(newNode);
        }
    }

    private void remove(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addFirst(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
