package com.interview.event;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

/**
 * Demonstrates Java NIO (New IO / Non-Blocking IO).
 * writes events to a file channel efficiently using ByteBuffers.
 */
public class NioEventLogger implements AutoCloseable {

    private final FileChannel fileChannel;

    public NioEventLogger(Path path) throws IOException {
        this.fileChannel = FileChannel.open(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND);
    }

    public void log(Event event) throws IOException {
        String logEntry = String.format("[%s] %s%n", Instant.now(), event.toString());
        byte[] bytes = logEntry.getBytes(StandardCharsets.UTF_8);

        // Wrap bytes into a Buffer
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        // Write to channel
        // In a real non-blocking app, we would handle partial writes and use a
        // Selector.
        // For this demo, we assume the OS accepts the write.
        while (buffer.hasRemaining()) {
            fileChannel.write(buffer);
        }
        // Force update to disk (metadata=false)
        fileChannel.force(false);
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }
}
