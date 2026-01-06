package com.visualizer.instrumentation.associative;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

import java.util.HashSet;
import java.util.Set;

/**
 * Instrumented HashSet wrapper that emits visualization steps.
 */
public class TrackedSet<T> {

    private final String name;
    private final Set<T> data;
    private final StepCollector collector;

    public TrackedSet(String name) {
        this.name = name;
        this.data = new HashSet<>();
        this.collector = StepCollector.getInstance();

        collector.emit(
                new Step(0, StepAction.SET_ADD, "Initialize HashSet '" + name + "'")
                        .withTarget(name)
                        .withData("type", "SET")
                        .withState(new HashSet<>(data)));
    }

    public boolean add(T value) {
        boolean added = data.add(value);
        collector.emit(
                new Step(0, StepAction.SET_ADD, name + ".add(" + value + ")")
                        .withTarget(name)
                        .withData("type", "SET")
                        .withValue(value)
                        .withData("added", added)
                        .withHighlight("ACTIVE")
                        .withState(new HashSet<>(data)));
        return added;
    }

    public boolean contains(T value) {
        boolean result = data.contains(value);
        collector.emit(
                new Step(0, StepAction.SET_CONTAINS, name + ".contains(" + value + ") = " + result)
                        .withTarget(name)
                        .withData("type", "SET")
                        .withValue(value)
                        .withData("result", result)
                        .withHighlight(result ? "FOUND" : "NOT_FOUND")
                        .withState(new HashSet<>(data)));
        return result;
    }

    public boolean remove(T value) {
        boolean removed = data.remove(value);
        collector.emit(
                new Step(0, StepAction.SET_REMOVE, name + ".remove(" + value + ")")
                        .withTarget(name)
                        .withData("type", "SET")
                        .withValue(value)
                        .withHighlight("ACTIVE")
                        .withState(new HashSet<>(data)));
        return removed;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public Set<T> toSet() {
        return new HashSet<>(data);
    }

    public String getName() {
        return name;
    }
}
