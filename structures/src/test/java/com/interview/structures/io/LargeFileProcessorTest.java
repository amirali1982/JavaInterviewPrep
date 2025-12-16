package com.interview.structures.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class LargeFileProcessorTest {

    @TempDir
    Path tempDir;

    @Test
    void testMappedReadWrite() throws Exception {
        LargeFileProcessor processor = new LargeFileProcessor();
        Path file = tempDir.resolve("large_data.bin");
        String content = "Zero Copy IO is Fast!";

        processor.writeData(file, content);

        String result = processor.readData(file, content.getBytes().length);

        assertEquals(content, result);
    }
}
