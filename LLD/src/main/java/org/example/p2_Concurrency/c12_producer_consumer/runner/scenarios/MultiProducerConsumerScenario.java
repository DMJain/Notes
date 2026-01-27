package org.example.p2_Concurrency.c12_producer_consumer.runner.scenarios;

import org.example.p2_Concurrency.c12_producer_consumer.impl.SemaphoreBuffer;
import org.example.p2_Concurrency.c12_producer_consumer.impl.TShirtConsumer;
import org.example.p2_Concurrency.c12_producer_consumer.impl.TShirtProducer;
import org.example.p2_Concurrency.c12_producer_consumer.model.TShirt;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced scenario with multiple producers and consumers.
 * 
 * <p>
 * Demonstrates how semaphores handle contention when many
 * threads compete for buffer access.
 * </p>
 */
public class MultiProducerConsumerScenario {

    private static final int BUFFER_CAPACITY = 5;
    private static final int PRODUCER_COUNT = 3;
    private static final int CONSUMER_COUNT = 5;
    private static final int ITEMS_PER_PRODUCER = 2;

    public void execute() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("     MULTI PRODUCER-CONSUMER SCENARIO");
        System.out.println("═".repeat(60));
        System.out.printf("Buffer: %d | Producers: %d | Consumers: %d%n",
                BUFFER_CAPACITY, PRODUCER_COUNT, CONSUMER_COUNT);
        System.out.printf("Items per producer: %d | Total items: %d%n%n",
                ITEMS_PER_PRODUCER, PRODUCER_COUNT * ITEMS_PER_PRODUCER);

        // Shared buffer
        SemaphoreBuffer<TShirt> buffer = new SemaphoreBuffer<>(BUFFER_CAPACITY);

        // Calculate items per consumer (may have some idle consumers)
        int totalItems = PRODUCER_COUNT * ITEMS_PER_PRODUCER;
        int itemsPerConsumer = totalItems / CONSUMER_COUNT;
        int extraItems = totalItems % CONSUMER_COUNT;

        List<Thread> threads = new ArrayList<>();

        // Create producers
        for (int i = 1; i <= PRODUCER_COUNT; i++) {
            TShirtProducer producer = new TShirtProducer(
                    "Factory-" + i, buffer, ITEMS_PER_PRODUCER);
            Thread t = new Thread(producer, "Producer-" + i);
            threads.add(t);
        }

        // Create consumers
        for (int i = 1; i <= CONSUMER_COUNT; i++) {
            int items = itemsPerConsumer + (i <= extraItems ? 1 : 0);
            if (items > 0) {
                TShirtConsumer consumer = new TShirtConsumer(
                        "Customer-" + i, buffer, items);
                Thread t = new Thread(consumer, "Consumer-" + i);
                threads.add(t);
            }
        }

        // Start all threads
        System.out.println("🚀 Starting all producers and consumers...\n");
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n✅ Multi producer-consumer scenario completed!");
        buffer.displayStatus();
    }
}
