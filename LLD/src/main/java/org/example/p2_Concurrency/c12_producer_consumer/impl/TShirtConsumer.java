package org.example.p2_Concurrency.c12_producer_consumer.impl;

import org.example.p2_Concurrency.c12_producer_consumer.contracts.IBuffer;
import org.example.p2_Concurrency.c12_producer_consumer.contracts.IConsumer;
import org.example.p2_Concurrency.c12_producer_consumer.model.TShirt;

import java.util.Random;

/**
 * T-Shirt customer consumer implementation.
 * 
 * <p>
 * Consumes T-Shirts from a shared buffer.
 * Simulates consumption time with random delays.
 * </p>
 */
public class TShirtConsumer implements IConsumer<TShirt> {

    private final String name;
    private final IBuffer<TShirt> buffer;
    private final int itemsToConsume;
    private final Random random;

    /**
     * Creates a T-Shirt consumer.
     * 
     * @param name           consumer identifier
     * @param buffer         shared buffer to take items from
     * @param itemsToConsume number of items to consume (-1 for infinite)
     */
    public TShirtConsumer(String name, IBuffer<TShirt> buffer, int itemsToConsume) {
        this.name = name;
        this.buffer = buffer;
        this.itemsToConsume = itemsToConsume;
        this.random = new Random();
    }

    @Override
    public void run() {
        System.out.printf("🛒 [%s] Consumer started (will take %s items)%n",
                name, itemsToConsume == -1 ? "infinite" : itemsToConsume);

        int consumed = 0;
        try {
            while (itemsToConsume == -1 || consumed < itemsToConsume) {
                // Take from buffer (blocks if empty)
                TShirt shirt = buffer.take();

                // Consume the item
                consume(shirt);
                consumed++;

                // Simulate consumption time
                Thread.sleep(random.nextInt(800) + 300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("❌ [%s] Consumer interrupted%n", name);
        }

        System.out.printf("🛒 [%s] Consumer finished (consumed %d items)%n", name, consumed);
    }

    @Override
    public void consume(TShirt item) {
        System.out.printf("   [%s] 🎉 Bought: %s%n", name, item);
    }

    @Override
    public String getName() {
        return name;
    }
}
