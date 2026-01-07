package com.visualizer.instrumentation.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that collects visualization steps during execution.
 * Thread-local to support concurrent executions.
 */
public class StepCollector {

    private static final ThreadLocal<StepCollector> instance = ThreadLocal.withInitial(StepCollector::new);

    private final List<Step> steps = new ArrayList<>();
    private boolean recording = false;
    private int currentLineNumber = -1; // Track current source line for auto-instrumentation

    private StepCollector() {
    }

    public static StepCollector getInstance() {
        return instance.get();
    }

    public static void reset() {
        instance.remove();
    }

    public void startRecording() {
        steps.clear();
        recording = true;
        currentLineNumber = -1;
    }

    public void stopRecording() {
        recording = false;
    }

    public void setCurrentLine(int lineNumber) {
        this.currentLineNumber = lineNumber;
    }

    public void emit(Step step) {
        if (recording) {
            // Auto-set line number if not already set and we have a current line
            if (step.getLineNumber() <= 0 && currentLineNumber > 0) {
                step = step.withLineNumber(currentLineNumber);
            }
            steps.add(step);
        }
    }

    public List<Step> getSteps() {
        return new ArrayList<>(steps);
    }

    public boolean isRecording() {
        return recording;
    }
}
