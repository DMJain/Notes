package org.example.p2_Concurrency.c12_producer_consumer.contracts;

/**
 * Contract for a consumer that processes items.
 * 
 * @param <T> the type of items consumed
 */
public interface IConsumer<T> extends Runnable {

    /**
     * Consumes and processes an item.
     * 
     * @param item the item to consume
     */
    void consume(T item);

    /**
     * Gets the consumer's name/identifier.
     * 
     * @return consumer name
     */
    String getName();
}
