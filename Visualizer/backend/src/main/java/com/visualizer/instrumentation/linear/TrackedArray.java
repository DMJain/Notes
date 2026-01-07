package com.visualizer.instrumentation.linear;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

/**
 * Wrapper for int[] that emits visualization steps.
 */
public class TrackedArray {

    private final String name;
    private final int[] data;

    public TrackedArray(String name, int size) {
        this.name = name;
        this.data = new int[size];
        emitInit();
    }

    public TrackedArray(String name, int[] data) {
        this.name = name;
        this.data = data.clone();
        emitInit();
    }

    private void emitInit() {
        Step step = new Step(0, StepAction.ARRAY_INIT, "Initialize array '" + name + "'")
                .withTarget(name)
                .withState(data.clone());
        StepCollector.getInstance().emit(step);
    }

    public int get(int index) {
        int value = data[index];
        Step step = new Step(0, StepAction.ARRAY_GET, "Read " + name + "[" + index + "] = " + value)
                .withTarget(name)
                .withIndex(index)
                .withValue(value)
                .withState(data.clone())
                .withHighlight("ACTIVE");
        StepCollector.getInstance().emit(step);
        return value;
    }

    public void set(int index, int value) {
        int oldValue = data[index];
        data[index] = value;
        Step step = new Step(0, StepAction.ARRAY_SET, "Set " + name + "[" + index + "] = " + value)
                .withTarget(name)
                .withIndex(index)
                .withValue(value)
                .withOldValue(oldValue)
                .withState(data.clone())
                .withHighlight("ACTIVE");
        StepCollector.getInstance().emit(step);
    }

    public void highlight(int index, String highlightType) {
        Step step = new Step(0, StepAction.ARRAY_HIGHLIGHT, "Highlight " + name + "[" + index + "]")
                .withTarget(name)
                .withIndex(index)
                .withState(data.clone())
                .withHighlight(highlightType);
        StepCollector.getInstance().emit(step);
    }

    public int length() {
        return data.length;
    }

    public int[] toArray() {
        return data.clone();
    }

    @Override
    public String toString() {
        return java.util.Arrays.toString(data);
    }
}
