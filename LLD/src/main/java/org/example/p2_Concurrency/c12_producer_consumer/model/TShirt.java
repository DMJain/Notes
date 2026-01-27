package org.example.p2_Concurrency.c12_producer_consumer.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a T-Shirt item in the Producer-Consumer demo.
 * 
 * <p>
 * Each T-Shirt has a unique ID and tracks its creation time
 * and which producer created it.
 * </p>
 */
public class TShirt {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int id;
    private final String producerName;
    private final Instant createdAt;
    private final String color;

    public TShirt(String producerName, String color) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.producerName = producerName;
        this.color = color;
        this.createdAt = Instant.now();
    }

    public int getId() {
        return id;
    }

    public String getProducerName() {
        return producerName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return String.format("👕 TShirt[id=%d, color=%s, by=%s]",
                id, color, producerName);
    }
}
