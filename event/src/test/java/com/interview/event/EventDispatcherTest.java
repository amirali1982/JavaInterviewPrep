package com.interview.event;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventDispatcherTest {

    // Logic: Specific Event Type
    static class DownloadProgressEvent implements Event {
        final int progress;

        DownloadProgressEvent(int progress) {
            this.progress = progress;
        }
    }

    // Logic: Specific Component (The "Source")
    static class DownloadManager {
        // "I have a megaphone specifically for progress updates"
        public final EventDispatcher<DownloadProgressEvent> onProgress = new EventDispatcher<>();

        void startDownload() {
            // Simulate download steps
            onProgress.dispatch(new DownloadProgressEvent(0));
            onProgress.dispatch(new DownloadProgressEvent(50));
            onProgress.dispatch(new DownloadProgressEvent(100));
        }
    }

    @Test
    void testMegaphonePattern() {
        DownloadManager downloader = new DownloadManager();
        AtomicInteger progressSum = new AtomicInteger(0);

        // User connects specifically to ONLY this downloader's progress megaphone
        downloader.onProgress.addListener(event -> {
            System.out.println("Received progress: " + event.progress);
            progressSum.addAndGet(event.progress);
        });

        downloader.startDownload();

        assertEquals(150, progressSum.get(), "Should have received all specific progress events (0 + 50 + 100)");
    }
}
