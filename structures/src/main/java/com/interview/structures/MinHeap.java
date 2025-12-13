package com.interview.structures;

import java.util.Arrays;

/**
 * MinHeap implementation (Basis for PriorityQueue).
 * Validates: Parent <= Children.
 */
public class MinHeap {
    private int[] heap;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public MinHeap() {
        heap = new int[DEFAULT_CAPACITY];
    }

    public void add(int val) {
        ensureCapacity();
        heap[size] = val;
        bubbleUp(size);
        size++;
    }

    public int poll() {
        if (size == 0)
            throw new IllegalStateException("Heap is empty");

        int item = heap[0];
        // Move last element to root
        heap[0] = heap[size - 1];
        size--;
        // Fix heap property downwards
        heapifyDown();
        return item;
    }

    public int peek() {
        if (size == 0)
            throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    private void bubbleUp(int index) {
        while (hasParent(index) && parent(index) > heap[index]) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    private void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index) < leftChild(index)) {
                smallerChildIndex = getRightChildIndex(index);
            }

            if (heap[index] < heap[smallerChildIndex]) {
                break;
            } else {
                swap(index, smallerChildIndex);
            }
            index = smallerChildIndex;
        }
    }

    // Helpers
    private int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    private int getLeftChildIndex(int i) {
        return 2 * i + 1;
    }

    private int getRightChildIndex(int i) {
        return 2 * i + 2;
    }

    private boolean hasParent(int i) {
        return getParentIndex(i) >= 0;
    }

    private boolean hasLeftChild(int i) {
        return getLeftChildIndex(i) < size;
    }

    private boolean hasRightChild(int i) {
        return getRightChildIndex(i) < size;
    }

    private int parent(int i) {
        return heap[getParentIndex(i)];
    }

    private int leftChild(int i) {
        return heap[getLeftChildIndex(i)];
    }

    private int rightChild(int i) {
        return heap[getRightChildIndex(i)];
    }

    private void swap(int indexOne, int indexTwo) {
        int temp = heap[indexOne];
        heap[indexOne] = heap[indexTwo];
        heap[indexTwo] = temp;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }
}
