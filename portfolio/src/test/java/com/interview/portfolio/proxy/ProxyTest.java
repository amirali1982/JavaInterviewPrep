package com.interview.portfolio.proxy;

import com.interview.portfolio.Asset;
import com.interview.portfolio.GenericRepository;
import com.interview.portfolio.MapBasedGenericRepository;
import com.interview.portfolio.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProxyTest {

    @Test
    @SuppressWarnings("unchecked")
    void testLoggingProxyInterception() {
        // 1. Create the real object
        GenericRepository<Asset> realRepo = new MapBasedGenericRepository<>();

        // 2. Create the proxy
        GenericRepository<Asset> proxyRepo = LoggingHandler.createProxy(realRepo, GenericRepository.class);

        // 3. Capture StdOut to verify logging
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // 4. Call method marked with @Logged
            Asset apple = new Stock("AAPL", "Apple", "Tech", new BigDecimal("150"));
            proxyRepo.add(apple);

            // 5. Verify logic execution (Real object was called)
            assertTrue(realRepo.findById("AAPL").isPresent());

            // 6. Verify logging (Proxy intercepted)
            String output = outContent.toString();
            assertTrue(output.contains("[PROXY] Entering method: add"), "Should log entry");
            assertTrue(output.contains("[PROXY] Exiting method: add"), "Should log exit");

        } finally {
            // Restore StdOut
            System.setOut(System.out);
        }
    }
}
