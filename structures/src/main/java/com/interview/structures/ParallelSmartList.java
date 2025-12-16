package com.interview.structures;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A Custom List that implements a custom Spliterator.
 * Demonstrates: How Parallel Streams work under the hood.
 */
public class ParallelSmartList<T> implements Iterable<T> {
    private final Object[] elements;
    private final int size;

    public ParallelSmartList(T[] input) {
        this.elements = input; // clone in real world
        this.size = input.length;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                return (T) elements[index++];
            }
        };
    }

    @Override
    public Spliterator<T> spliterator() {
        return new CustomArrSpliterator<>(elements, 0, size);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    /**
     * Custom Spliterator.
     * Logic: Splits the array range in half recursively.
     */
    static class CustomArrSpliterator<T> implements Spliterator<T> {
        private final Object[] array;
        private int current;
        private final int fence; // one past last index

        public CustomArrSpliterator(Object[] array, int current, int fence) {
            this.array = array;
            this.current = current;
            this.fence = fence;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (current < fence) {
                @SuppressWarnings("unchecked")
                T item = (T) array[current++];
                action.accept(item);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            int lo = current;
            int mid = (lo + fence) >>> 1;
            // If range is too small, don't split
            if (lo >= mid) {
                return null;
            }
            current = mid; // This spliterator now covers [mid, fence)
            // Return new spliterator covering [lo, mid)
            return new CustomArrSpliterator<>(array, lo, mid);
        }

        @Override
        public long estimateSize() {
            return (long) (fence - current);
        }

        @Override
        public int characteristics() {
            return ORDERED | SIZED | SUBSIZED | NONNULL | IMMUTABLE;
        }
    }
}
