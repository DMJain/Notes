package com.visualizer.instrumentation.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single visualization step
 */
public class Step {
    private int stepId;
    private StepAction action;
    private String description;
    private int lineNumber;
    private Map<String, Object> data;

    public Step() {
        this.data = new HashMap<>();
    }

    public Step(int stepId, StepAction action, String description) {
        this.stepId = stepId;
        this.action = action;
        this.description = description;
        this.lineNumber = -1;
        this.data = new HashMap<>();
    }

    // Fluent setters for building
    public Step withLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public Step withData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Step withTarget(String targetName) {
        this.data.put("target", targetName);
        return this;
    }

    public Step withIndex(int index) {
        this.data.put("index", index);
        return this;
    }

    public Step withIndices(int... indices) {
        this.data.put("indices", indices);
        return this;
    }

    public Step withValue(Object value) {
        this.data.put("value", value);
        return this;
    }

    public Step withOldValue(Object oldValue) {
        this.data.put("oldValue", oldValue);
        return this;
    }

    public Step withHighlight(String color) {
        this.data.put("highlight", color);
        return this;
    }

    public Step withState(Object state) {
        this.data.put("state", state);
        return this;
    }

    // Getters
    public int getStepId() {
        return stepId;
    }

    public StepAction getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Map<String, Object> getData() {
        return data;
    }

    // Setters
    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public void setAction(StepAction action) {
        this.action = action;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
