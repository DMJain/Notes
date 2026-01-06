package com.visualizer.instrumentation.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread-local collector for visualization steps.
 * All TrackedDataStructure instances emit steps to this collector.
 */
public class StepCollector {

    private static final ThreadLocal<StepCollector> INSTANCE = ThreadLocal.withInitial(StepCollector::new);

    private List<Step> steps;
    private int stepCounter;
    private boolean recording;

    public StepCollector() {
        this.steps = new ArrayList<>();
        this.stepCounter = 0;
        this.recording = false;
    }

    public static StepCollector getInstance() {
        return INSTANCE.get();
    }

    public static void reset() {
        INSTANCE.remove();
    }

    public void startRecording() {
        this.steps.clear();
        this.stepCounter = 0;
        this.recording = true;
    }

    public void stopRecording() {
        this.recording = false;
    }

    public boolean isRecording() {
        return recording;
    }

    public void emit(StepAction action, String description) {
        if (!recording)
            return;
        Step step = new Step(++stepCounter, action, description);
        steps.add(step);
    }

    public Step emit(Step step) {
        if (!recording)
            return step;
        step.setStepId(++stepCounter);
        steps.add(step);
        return step;
    }

    public Step createStep(StepAction action, String description) {
        return new Step(0, action, description); // stepId assigned on emit
    }

    public List<Step> getSteps() {
        return new ArrayList<>(steps);
    }

    public int getStepCount() {
        return steps.size();
    }
}
