package org.example.p2_Concurrency.c9_volatile_keyword.contracts;

/**
 * Contract for a flag that can be toggled and read across threads.
 */
public interface IFlag {
    void setTrue();

    void setFalse();

    boolean getValue();
}
