package org.example.p2_Concurrency.c9_volatile_keyword.impl;

import org.example.p2_Concurrency.c9_volatile_keyword.contracts.IFlag;

/**
 * Flag implementation WITH volatile - ensures visibility.
 */
public class VolatileFlag implements IFlag {

    private volatile boolean flag = false;

    @Override
    public void setTrue() {
        flag = true; // Writes to main memory immediately
    }

    @Override
    public void setFalse() {
        flag = false;
    }

    @Override
    public boolean getValue() {
        return flag; // Reads from main memory
    }
}
