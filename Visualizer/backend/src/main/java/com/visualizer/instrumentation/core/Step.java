package com.visualizer.instrumentation.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * Returns targets in the format expected by frontend:
     * [{type, name, state, index, indices, value, highlight}]
     */
    public List<Map<String, Object>> getTargets() {
        List<Map<String, Object>> targets = new ArrayList<>();
        if (data.containsKey("target") || data.containsKey("state")) {
            Map<String, Object> target = new HashMap<>();
            // Derive type from action
            target.put("type", getTargetTypeFromAction());
            target.put("name", data.getOrDefault("target", "unknown"));
            if (data.containsKey("state"))
                target.put("state", data.get("state"));
            if (data.containsKey("index"))
                target.put("index", data.get("index"));
            if (data.containsKey("indices"))
                target.put("indices", data.get("indices"));
            if (data.containsKey("value"))
                target.put("value", data.get("value"));
            if (data.containsKey("key"))
                target.put("key", data.get("key"));
            if (data.containsKey("highlight"))
                target.put("highlight", data.get("highlight"));
            targets.add(target);
        }
        return targets;
    }

    private String getTargetTypeFromAction() {
        if (action == null)
            return "VARIABLE";
        return switch (action) {
            case ARRAY_INIT, ARRAY_GET, ARRAY_SET, ARRAY_SWAP, ARRAY_HIGHLIGHT -> "ARRAY";
            case MAP_INIT, MAP_PUT, MAP_GET, MAP_REMOVE, MAP_CONTAINS -> "MAP";
            case SET_ADD, SET_REMOVE, SET_CONTAINS -> "SET";
            case VAR_INIT, VAR_UPDATE -> "VARIABLE";
            case POINTER_MOVE -> "POINTER";
            default -> "VARIABLE";
        };
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
