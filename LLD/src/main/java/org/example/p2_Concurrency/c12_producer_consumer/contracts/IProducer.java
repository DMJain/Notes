package org.example.p2_Concurrency.c12_producer_consumer.contracts;

/**
 * Contract for a producer that generates items.
 * 
 * @param <T> the type of items produced
 */
public interface IProducer<T> extends Runnable {

    /**
     * Produces a single item.
     * 
     * @return the produced item
     */
    T produce();

    /**
     * Gets the producer's name/identifier.
     * 
     * @return producer name
     */
    String getName();
}
