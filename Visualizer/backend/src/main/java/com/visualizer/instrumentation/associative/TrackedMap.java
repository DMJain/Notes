package com.visualizer.instrumentation.associative;

import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;

import java.util.HashMap;
import java.util.Map;

/**
 * Instrumented HashMap wrapper that emits visualization steps on every
 * operation.
 */
public class TrackedMap<K, V> {

    private final String name;
    private final Map<K, V> data;
    private final StepCollector collector;

    public TrackedMap(String name) {
        this.name = name;
        this.data = new HashMap<>();
        this.collector = StepCollector.getInstance();

        collector.emit(
                new Step(0, StepAction.MAP_INIT, "Initialize HashMap '" + name + "'")
                        .withTarget(name)
                        .withState(new HashMap<>(data)));
    }

    public V put(K key, V value) {
        V oldValue = data.put(key, value);
        collector.emit(
                new Step(0, StepAction.MAP_PUT, name + ".put(" + key + ", " + value + ")")
                        .withTarget(name)
                        .withData("key", key)
                        .withValue(value)
                        .withOldValue(oldValue)
                        .withHighlight("ACTIVE")
                        .withState(new HashMap<>(data)));
        return oldValue;
    }

    public V get(K key) {
        V value = data.get(key);
        collector.emit(
                new Step(0, StepAction.MAP_GET, name + ".get(" + key + ") = " + value)
                        .withTarget(name)
                        .withData("key", key)
                        .withValue(value)
                        .withData("found", value != null)
                        .withHighlight(value != null ? "FOUND" : "NOT_FOUND")
                        .withState(new HashMap<>(data)));
        return value;
    }

    public boolean containsKey(K key) {
        boolean result = data.containsKey(key);
        collector.emit(
                new Step(0, StepAction.MAP_CONTAINS, name + ".containsKey(" + key + ") = " + result)
                        .withTarget(name)
                        .withData("key", key)
                        .withData("result", result)
                        .withHighlight(result ? "FOUND" : "NOT_FOUND")
                        .withState(new HashMap<>(data)));
        return result;
    }

    public V remove(K key) {
        V value = data.remove(key);
        collector.emit(
                new Step(0, StepAction.MAP_REMOVE, name + ".remove(" + key + ")")
                        .withTarget(name)
                        .withData("key", key)
                        .withValue(value)
                        .withHighlight("ACTIVE")
                        .withState(new HashMap<>(data)));
        return value;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public Map<K, V> toMap() {
        return new HashMap<>(data);
    }

    public String getName() {
        return name;
    }
}
