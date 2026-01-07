package com.visualizer.instrumentation.linear;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

/**
 * Wrapper for String that emits visualization steps.
 * Treats the string as a char array for pointer visualization.
 */
public class TrackedString {

    private final String name;
    private final String data;
    private final char[] chars;

    public TrackedString(String name, String data) {
        this.name = name;
        this.data = data;
        this.chars = data.toCharArray();
        emitInit();
    }

    private void emitInit() {
        Step step = new Step(0, StepAction.ARRAY_INIT, "Initialize string '" + name + "' = \"" + data + "\"")
                .withTarget(name)
                .withData("type", "CHAR_ARRAY")
                .withState(toStringArray());
        StepCollector.getInstance().emit(step);
    }

    public char charAt(int index) {
        char value = chars[index];
        Step step = new Step(0, StepAction.ARRAY_GET, "Read " + name + "[" + index + "] = '" + value + "'")
                .withTarget(name)
                .withIndex(index)
                .withValue(String.valueOf(value))
                .withData("type", "CHAR_ARRAY")
                .withState(toStringArray())
                .withHighlight("ACTIVE");
        StepCollector.getInstance().emit(step);
        return value;
    }

    public int length() {
        return data.length();
    }

    public String getValue() {
        return data;
    }

    /**
     * Highlight a range of characters (for sliding window visualization).
     */
    public void highlightRange(int left, int right, String highlightType) {
        int[] indices = new int[right - left + 1];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = left + i;
        }
        Step step = new Step(0, StepAction.ARRAY_HIGHLIGHT, "Highlight " + name + "[" + left + ":" + right + "]")
                .withTarget(name)
                .withIndices(indices)
                .withData("type", "CHAR_ARRAY")
                .withState(toStringArray())
                .withHighlight(highlightType);
        StepCollector.getInstance().emit(step);
    }

    /**
     * Highlight a single character.
     */
    public void highlight(int index, String highlightType) {
        Step step = new Step(0, StepAction.ARRAY_HIGHLIGHT, "Highlight " + name + "[" + index + "]")
                .withTarget(name)
                .withIndex(index)
                .withData("type", "CHAR_ARRAY")
                .withState(toStringArray())
                .withHighlight(highlightType);
        StepCollector.getInstance().emit(step);
    }

    private String[] toStringArray() {
        String[] arr = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            arr[i] = String.valueOf(chars[i]);
        }
        return arr;
    }

    @Override
    public String toString() {
        return data;
    }
}
