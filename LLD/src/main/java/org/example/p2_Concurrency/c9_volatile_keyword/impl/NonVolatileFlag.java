package org.example.p2_Concurrency.c9_volatile_keyword.impl;

import org.example.p2_Concurrency.c9_volatile_keyword.contracts.IFlag;

/**
 * Flag implementation WITHOUT volatile - may cause visibility issues.
 * 
 * <p>
 * ⚠️ This is intentionally problematic for demonstration purposes!
 * Reader threads may not see updates due to CPU caching.
 * </p>
 */
public class NonVolatileFlag implements IFlag {

    private boolean flag = false; // NOT volatile!

    @Override
    public void setTrue() {
        flag = true; // May stay in thread's local cache
    }

    @Override
    public void setFalse() {
        flag = false;
    }

    @Override
    public boolean getValue() {
        return flag; // May read from stale cache
    }
}
