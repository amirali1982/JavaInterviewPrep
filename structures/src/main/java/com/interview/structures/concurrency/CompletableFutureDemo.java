package com.interview.structures.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Demonstrates CompletableFuture (Java 8+) for asynchronous programming.
 * Replacing legacy Future<T> which required blocking .get().
 */
public class CompletableFutureDemo {

    public String chainTasks() throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            // Task 1: Fetch Data (simulated)
            return "Data";
        }).thenApply(data -> {
            // Task 2: Process Data (transform)
            return "Processed " + data;
        }).thenCompose(processed -> {
            // Task 3: Chain another async task (like flatMap)
            return CompletableFuture.supplyAsync(() -> processed + " & Saved");
        }).get(); // Join at the end
    }

    public String handleException() throws ExecutionException, InterruptedException {
        return CompletableFuture.<String>supplyAsync(() -> {
            throw new RuntimeException("Network Error");
        }).exceptionally(ex -> {
            // Recover from error
            return "Default Value";
        }).get();
    }

    public String combineTasks() throws ExecutionException, InterruptedException {
        CompletableFuture<String> api1 = CompletableFuture.supplyAsync(() -> "Price: 100");
        CompletableFuture<String> api2 = CompletableFuture.supplyAsync(() -> "Tax: 10");

        // Wait for BOTH to finish, then combine
        return api1.thenCombine(api2, (price, tax) -> price + " + " + tax).get();
    }
}
