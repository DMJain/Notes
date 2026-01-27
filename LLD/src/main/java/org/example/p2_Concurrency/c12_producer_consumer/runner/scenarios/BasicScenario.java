package org.example.p2_Concurrency.c12_producer_consumer.runner.scenarios;

import org.example.p2_Concurrency.c12_producer_consumer.impl.SemaphoreBuffer;
import org.example.p2_Concurrency.c12_producer_consumer.impl.TShirtConsumer;
import org.example.p2_Concurrency.c12_producer_consumer.impl.TShirtProducer;
import org.example.p2_Concurrency.c12_producer_consumer.model.TShirt;

/**
 * Basic Producer-Consumer scenario with single producer and consumer.
 */
public class BasicScenario {

    private static final int BUFFER_CAPACITY = 5;
    private static final int ITEMS_PER_ACTOR = 3;

    public void execute() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("     BASIC PRODUCER-CONSUMER SCENARIO");
        System.out.println("═".repeat(60));
        System.out.printf("Buffer Capacity: %d | Items per actor: %d%n%n",
                BUFFER_CAPACITY, ITEMS_PER_ACTOR);

        // Create shared buffer
        SemaphoreBuffer<TShirt> buffer = new SemaphoreBuffer<>(BUFFER_CAPACITY);

        // Create producer and consumer
        TShirtProducer producer = new TShirtProducer("Factory-1", buffer, ITEMS_PER_ACTOR);
        TShirtConsumer consumer = new TShirtConsumer("Customer-1", buffer, ITEMS_PER_ACTOR);

        // Create threads
        Thread producerThread = new Thread(producer, "Producer-1");
        Thread consumerThread = new Thread(consumer, "Consumer-1");

        // Start threads
        System.out.println("🚀 Starting producer and consumer...\n");
        producerThread.start();
        consumerThread.start();

        // Wait for completion
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✅ Basic scenario completed!");
        buffer.displayStatus();
    }
}
