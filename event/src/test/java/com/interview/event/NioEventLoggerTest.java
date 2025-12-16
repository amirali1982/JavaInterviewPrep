package com.interview.event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NioEventLoggerTest {

    @TempDir
    Path tempDir;

    private NioEventLogger logger;
    private Path logFile;

    @BeforeEach
    void setUp() throws Exception {
        logFile = tempDir.resolve("events.log");
        logger = new NioEventLogger(logFile);
    }

    @AfterEach
    void tearDown() throws Exception {
        logger.close();
    }

    static class TestEvent implements Event {
        private final String msg;

        TestEvent(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "Event: " + msg;
        }
    }

    @Test
    void testLogWritesToFile() throws Exception {
        logger.log(new TestEvent("Login"));
        logger.log(new TestEvent("Logout"));

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("Event: Login"));
        assertTrue(lines.get(1).contains("Event: Logout"));
    }
}
