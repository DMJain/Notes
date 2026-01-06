package com.visualizer.instrumentation.core;

import java.util.Arrays;

/**
 * Tracks primitive variables (int, long, boolean, String, etc.)
 * Can optionally be marked as a "pointer" to indicate it indexes into an array.
 */
public class TrackedVariable<T> {

    private final String name;
    private T value;
    private final StepCollector collector;
    private boolean isPointer;
    private String targetArrayName; // If this is a pointer, which array does it point to?

    public TrackedVariable(String name, T initialValue) {
        this(name, initialValue, false, null);
    }

    /**
     * Create a tracked variable that acts as a pointer into an array
     */
    public TrackedVariable(String name, T initialValue, String targetArrayName) {
        this(name, initialValue, true, targetArrayName);
    }

    private TrackedVariable(String name, T initialValue, boolean isPointer, String targetArrayName) {
        this.name = name;
        this.value = initialValue;
        this.isPointer = isPointer;
        this.targetArrayName = targetArrayName;
        this.collector = StepCollector.getInstance();

        // Emit initialization
        Step step = new Step(0, StepAction.VAR_INIT, "Initialize " + name + " = " + initialValue)
                .withData("varName", name)
                .withValue(initialValue)
                .withData("isPointer", isPointer);

        if (isPointer && targetArrayName != null) {
            step.withTarget(targetArrayName)
                    .withData("pointerName", name)
                    .withIndex(toInt(initialValue));
        }

        collector.emit(step);
    }

    public T get() {
        return value;
    }

    public int getInt() {
        return toInt(value);
    }

    public void set(T newValue) {
        T oldValue = this.value;
        this.value = newValue;

        Step step = new Step(0, StepAction.VAR_UPDATE, name + ": " + oldValue + " → " + newValue)
                .withData("varName", name)
                .withValue(newValue)
                .withOldValue(oldValue)
                .withData("isPointer", isPointer)
                .withHighlight("ACTIVE");

        if (isPointer && targetArrayName != null) {
            step.withTarget(targetArrayName)
                    .withData("pointerName", name)
                    .withData("from", toInt(oldValue))
                    .withData("to", toInt(newValue));
        }

        collector.emit(step);
    }

    /**
     * Increment (for numeric types)
     */
    @SuppressWarnings("unchecked")
    public void increment() {
        if (value instanceof Integer) {
            set((T) Integer.valueOf(((Integer) value) + 1));
        } else if (value instanceof Long) {
            set((T) Long.valueOf(((Long) value) + 1));
        }
    }

    /**
     * Decrement (for numeric types)
     */
    @SuppressWarnings("unchecked")
    public void decrement() {
        if (value instanceof Integer) {
            set((T) Integer.valueOf(((Integer) value) - 1));
        } else if (value instanceof Long) {
            set((T) Long.valueOf(((Long) value) - 1));
        }
    }

    /**
     * Update with max
     */
    @SuppressWarnings("unchecked")
    public void updateMax(T candidate) {
        if (value instanceof Integer && candidate instanceof Integer) {
            int current = (Integer) value;
            int candidateVal = (Integer) candidate;
            if (candidateVal > current) {
                set(candidate);
            }
        }
    }

    /**
     * Update with min
     */
    @SuppressWarnings("unchecked")
    public void updateMin(T candidate) {
        if (value instanceof Integer && candidate instanceof Integer) {
            int current = (Integer) value;
            int candidateVal = (Integer) candidate;
            if (candidateVal < current) {
                set(candidate);
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean isPointer() {
        return isPointer;
    }

    public String getTargetArrayName() {
        return targetArrayName;
    }

    private int toInt(Object val) {
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return -1;
    }

    @Override
    public String toString() {
        return name + " = " + value;
    }
}
