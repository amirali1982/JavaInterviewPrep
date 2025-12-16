package com.interview.structures.linear;

import java.util.NoSuchElementException;

/**
 * A custom Deque (Double Ended Queue) implementation using a Doubly Linked
 * List.
 * Built from scratch to demonstrate pointers and node manipulation.
 *
 * @param <E> Element type
 */
public class MyDeque<E> {

    // Internal Node class
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    public void addLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null; // Help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        return element;
    }

    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null; // Help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        return element;
    }

    public E peekFirst() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.item;
    }
}
