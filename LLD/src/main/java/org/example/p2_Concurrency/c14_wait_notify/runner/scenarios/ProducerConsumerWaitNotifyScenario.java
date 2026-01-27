package org.example.p2_Concurrency.c14_wait_notify.runner.scenarios;

import org.example.p2_Concurrency.c14_wait_notify.impl.WaitNotifyBuffer;

/**
 * Producer-Consumer scenario using wait/notify.
 */
public class ProducerConsumerWaitNotifyScenario {

    private static final int BUFFER_CAPACITY = 3;
    private static final int ITEMS_TO_PRODUCE = 5;

    public void execute() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("     PRODUCER-CONSUMER WITH WAIT/NOTIFY");
        System.out.println("═".repeat(60));
        System.out.printf("Buffer Capacity: %d | Items: %d%n%n",
                BUFFER_CAPACITY, ITEMS_TO_PRODUCE);

        WaitNotifyBuffer<String> buffer = new WaitNotifyBuffer<>(BUFFER_CAPACITY);

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= ITEMS_TO_PRODUCE; i++) {
                    String item = "Item-" + i;
                    buffer.put(item);
                    Thread.sleep(200); // Simulate production time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   [Producer] Finished producing!");
        }, "Producer");

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= ITEMS_TO_PRODUCE; i++) {
                    String item = buffer.take();
                    System.out.printf("   [Consumer] 🎉 Consumed: %s%n", item);
                    Thread.sleep(300); // Simulate consumption time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   [Consumer] Finished consuming!");
        }, "Consumer");

        System.out.println("🚀 Starting producer and consumer...\n");
        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✅ Scenario completed!");
        buffer.displayStatus();
    }
}
