package com.visualizer.instrumentation.linear;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

import java.util.Arrays;

/**
 * Instrumented String/char array wrapper that shows characters (not ASCII
 * codes).
 */
public class TrackedCharArray {

    private final String name;
    private final char[] data;
    private final StepCollector collector;

    public TrackedCharArray(String name, String str) {
        this.name = name;
        this.data = str.toCharArray();
        this.collector = StepCollector.getInstance();

        collector.emit(
                new Step(0, StepAction.ARRAY_INIT, "Initialize char array '" + name + "' from \"" + str + "\"")
                        .withTarget(name)
                        .withData("type", "CHAR_ARRAY")
                        .withState(toStringArray()));
    }

    public char get(int index) {
        char value = data[index];
        collector.emit(
                new Step(0, StepAction.ARRAY_GET, "Read " + name + "[" + index + "] = '" + value + "'")
                        .withTarget(name)
                        .withData("type", "CHAR_ARRAY")
                        .withIndex(index)
                        .withValue(String.valueOf(value))
                        .withHighlight("ACTIVE")
                        .withState(toStringArray()));
        return value;
    }

    public void highlight(int index, String color) {
        collector.emit(
                new Step(0, StepAction.ARRAY_HIGHLIGHT, "Highlight " + name + "[" + index + "] = '" + data[index] + "'")
                        .withTarget(name)
                        .withData("type", "CHAR_ARRAY")
                        .withIndex(index)
                        .withHighlight(color)
                        .withState(toStringArray()));
    }

    public void highlightRange(int start, int end, String color) {
        int[] indices = new int[end - start + 1];
        for (int i = start; i <= end; i++) {
            indices[i - start] = i;
        }
        String substring = new String(data, start, end - start + 1);
        collector.emit(
                new Step(0, StepAction.ARRAY_HIGHLIGHT,
                        "Highlight " + name + "[" + start + ".." + end + "] = \"" + substring + "\"")
                        .withTarget(name)
                        .withData("type", "CHAR_ARRAY")
                        .withIndices(indices)
                        .withHighlight(color)
                        .withState(toStringArray()));
    }

    public int length() {
        return data.length;
    }

    public String getName() {
        return name;
    }

    /**
     * Convert char array to String array for display
     */
    private String[] toStringArray() {
        String[] result = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = String.valueOf(data[i]);
        }
        return result;
    }
}
