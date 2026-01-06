package com.visualizer.instrumentation.linear;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

import java.util.Arrays;

/**
 * Instrumented array wrapper that emits visualization steps on every operation.
 * Use this instead of primitive arrays in visualized solutions.
 */
public class TrackedArray {

    private final String name;
    private final int[] data;
    private final StepCollector collector;

    public TrackedArray(String name, int[] initialData) {
        this.name = name;
        this.data = Arrays.copyOf(initialData, initialData.length);
        this.collector = StepCollector.getInstance();

        // Emit initialization step
        collector.emit(
                new Step(0, StepAction.ARRAY_INIT, "Initialize array '" + name + "'")
                        .withTarget(name)
                        .withState(Arrays.copyOf(data, data.length)));
    }

    public TrackedArray(String name, int size) {
        this(name, new int[size]);
    }

    public int get(int index) {
        int value = data[index];
        collector.emit(
                new Step(0, StepAction.ARRAY_GET, "Read " + name + "[" + index + "] = " + value)
                        .withTarget(name)
                        .withIndex(index)
                        .withValue(value)
                        .withHighlight("ACTIVE")
                        .withState(Arrays.copyOf(data, data.length)));
        return value;
    }

    public void set(int index, int value) {
        int oldValue = data[index];
        data[index] = value;
        collector.emit(
                new Step(0, StepAction.ARRAY_SET, "Set " + name + "[" + index + "] = " + value)
                        .withTarget(name)
                        .withIndex(index)
                        .withValue(value)
                        .withOldValue(oldValue)
                        .withHighlight("ACTIVE")
                        .withState(Arrays.copyOf(data, data.length)));
    }

    public void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        collector.emit(
                new Step(0, StepAction.ARRAY_SWAP, "Swap " + name + "[" + i + "] ↔ " + name + "[" + j + "]")
                        .withTarget(name)
                        .withIndices(i, j)
                        .withHighlight("SWAPPING")
                        .withState(Arrays.copyOf(data, data.length)));
    }

    public void highlight(int index, String color) {
        collector.emit(
                new Step(0, StepAction.ARRAY_HIGHLIGHT, "Highlight " + name + "[" + index + "]")
                        .withTarget(name)
                        .withIndex(index)
                        .withHighlight(color)
                        .withState(Arrays.copyOf(data, data.length)));
    }

    public void highlightRange(int start, int end, String color) {
        int[] indices = new int[end - start + 1];
        for (int i = start; i <= end; i++) {
            indices[i - start] = i;
        }
        collector.emit(
                new Step(0, StepAction.ARRAY_HIGHLIGHT, "Highlight " + name + "[" + start + ".." + end + "]")
                        .withTarget(name)
                        .withIndices(indices)
                        .withHighlight(color)
                        .withState(Arrays.copyOf(data, data.length)));
    }

    public int length() {
        return data.length;
    }

    public int[] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " = " + Arrays.toString(data);
    }
}
