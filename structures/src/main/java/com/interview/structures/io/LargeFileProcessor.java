package com.interview.structures.io;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Demonstrates Processing "Large" Files using Memory Mapped IO.
 * MappedByteBuffer allows the OS to map a file directly into the virtual memory
 * space.
 * This avoids copying data from Kernel Space to User Space (Zero Copy),
 * essential for DBs/Kafka.
 */
public class LargeFileProcessor {

    /**
     * Writes data to a file using MappedByteBuffer.
     * Effectively we are writing to an "array" in memory, and the OS syncs it to
     * disk.
     */
    public void writeData(Path path, String data) throws IOException {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        try (FileChannel channel = FileChannel.open(path,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE)) {

            // Map the file into memory. Size must be known.
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);

            buffer.put(bytes);
            // buffer.force(); // Optional: force write to disk immediately
        }
    }

    /**
     * Reads data from file using MappedByteBuffer.
     */
    public String readData(Path path, int length) throws IOException {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);

            byte[] bytes = new byte[length];
            buffer.get(bytes); // Read directly from memory map

            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
