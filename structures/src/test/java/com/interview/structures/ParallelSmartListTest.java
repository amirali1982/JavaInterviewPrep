package com.interview.structures;

import org.junit.jupiter.api.Test;
import java.util.stream.Collectors;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ParallelSmartListTest {

    @Test
    void testParallelStream() {
        Integer[] nums = new Integer[1000];
        for (int i = 0; i < 1000; i++)
            nums[i] = i;

        ParallelSmartList<Integer> list = new ParallelSmartList<>(nums);

        // Verify output size
        List<Integer> result = list.parallelStream()
                .map(n -> n * 2)
                .collect(Collectors.toList());

        assertEquals(1000, result.size());

        // Verify Order is preserved (Spliterator.ORDERED)
        // Even in parallel, collect() usually respects order if Spliterator says so,
        // but exact processing order is async.
        assertEquals(0, result.get(0));
        assertEquals(1998, result.get(999));
    }
}
