package com.visualizer.instrumentation.linear;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

/**
 * Tracks pointer movements for two-pointer/sliding window algorithms.
 * Emits steps when pointers move.
 */
public class TrackedPointer {

    private final String name;
    private final String targetArray;
    private int position;
    private final StepCollector collector;

    public TrackedPointer(String name, String targetArray, int initialPosition) {
        this.name = name;
        this.targetArray = targetArray;
        this.position = initialPosition;
        this.collector = StepCollector.getInstance();

        collector.emit(
                new Step(0, StepAction.VAR_INIT, "Initialize pointer '" + name + "' at " + initialPosition)
                        .withTarget(targetArray)
                        .withData("pointerName", name)
                        .withIndex(initialPosition)
                        .withHighlight(getPointerColor()));
    }

    public int get() {
        return position;
    }

    public void moveTo(int newPosition) {
        int oldPosition = this.position;
        this.position = newPosition;
        collector.emit(
                new Step(0, StepAction.POINTER_MOVE, name + ": " + oldPosition + " → " + newPosition)
                        .withTarget(targetArray)
                        .withData("pointerName", name)
                        .withData("from", oldPosition)
                        .withData("to", newPosition)
                        .withHighlight(getPointerColor()));
    }

    public void increment() {
        moveTo(position + 1);
    }

    public void decrement() {
        moveTo(position - 1);
    }

    private String getPointerColor() {
        // Different colors for common pointer names
        return switch (name.toLowerCase()) {
            case "left", "l", "start", "i" -> "POINTER_LEFT";
            case "right", "r", "end", "j" -> "POINTER_RIGHT";
            default -> "POINTER_I";
        };
    }

    public String getName() {
        return name;
    }
}
