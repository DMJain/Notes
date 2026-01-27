package org.example.p2_Concurrency.c12_producer_consumer.impl;

import org.example.p2_Concurrency.c12_producer_consumer.contracts.IBuffer;
import org.example.p2_Concurrency.c12_producer_consumer.contracts.IProducer;
import org.example.p2_Concurrency.c12_producer_consumer.model.TShirt;

import java.util.Random;

/**
 * T-Shirt factory producer implementation.
 * 
 * <p>
 * Produces T-Shirts and places them in a shared buffer.
 * Simulates production time with random delays.
 * </p>
 */
public class TShirtProducer implements IProducer<TShirt> {

    private static final String[] COLORS = { "Red", "Blue", "Green", "Yellow", "Black", "White" };

    private final String name;
    private final IBuffer<TShirt> buffer;
    private final int itemsToProduce;
    private final Random random;

    /**
     * Creates a T-Shirt producer.
     * 
     * @param name           producer identifier
     * @param buffer         shared buffer to put items into
     * @param itemsToProduce number of items to produce (-1 for infinite)
     */
    public TShirtProducer(String name, IBuffer<TShirt> buffer, int itemsToProduce) {
        this.name = name;
        this.buffer = buffer;
        this.itemsToProduce = itemsToProduce;
        this.random = new Random();
    }

    @Override
    public void run() {
        System.out.printf("🏭 [%s] Producer started (will make %s items)%n",
                name, itemsToProduce == -1 ? "infinite" : itemsToProduce);

        int produced = 0;
        try {
            while (itemsToProduce == -1 || produced < itemsToProduce) {
                // Produce an item
                TShirt shirt = produce();

                // Put in buffer (blocks if full)
                buffer.put(shirt);
                produced++;

                // Simulate production time
                Thread.sleep(random.nextInt(500) + 200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("❌ [%s] Producer interrupted%n", name);
        }

        System.out.printf("🏭 [%s] Producer finished (produced %d items)%n", name, produced);
    }

    @Override
    public TShirt produce() {
        String color = COLORS[random.nextInt(COLORS.length)];
        return new TShirt(name, color);
    }

    @Override
    public String getName() {
        return name;
    }
}
