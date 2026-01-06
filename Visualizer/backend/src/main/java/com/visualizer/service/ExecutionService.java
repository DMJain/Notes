package com.visualizer.service;

import com.visualizer.dto.ExecutionResponse;
import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepAction;
import com.visualizer.instrumentation.core.StepCollector;
import com.visualizer.instrumentation.core.TrackedVariable;
import com.visualizer.instrumentation.linear.TrackedArray;
import com.visualizer.instrumentation.linear.TrackedCharArray;
import com.visualizer.instrumentation.associative.TrackedMap;
import com.visualizer.instrumentation.associative.TrackedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ExecutionService {

    private static final Logger log = LoggerFactory.getLogger(ExecutionService.class);
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Execute visualization for "Two Sum" problem
     * Line numbers map to TwoSum.java
     */
    public ExecutionResponse executeTwoSum(int[] nums, int target) {
        long startTime = System.currentTimeMillis();

        try {
            StepCollector collector = StepCollector.getInstance();
            collector.startRecording();

            // Line 8: method signature - input
            TrackedArray numsArr = new TrackedArray("nums", nums);
            setLastStepLine(collector, 8);

            // Line 9: int[] ans = new int[2]
            TrackedArray ans = new TrackedArray("ans", new int[] { 0, 0 });
            setLastStepLine(collector, 9);

            // Line 10: HashMap map = new HashMap<>()
            TrackedMap<Integer, Integer> map = new TrackedMap<>("map");
            setLastStepLine(collector, 10);

            // Target variable
            TrackedVariable<Integer> targetVar = new TrackedVariable<>("target", target);
            setLastStepLine(collector, 8);

            int[] result = null;

            // Line 11: for loop
            for (int idx = 0; idx < numsArr.length(); idx++) {
                TrackedVariable<Integer> i = new TrackedVariable<>("i", idx, "nums");
                setLastStepLine(collector, 11);

                int currentNum = numsArr.get(idx);
                setLastStepLine(collector, 11);

                // Line 13: calculate complement
                int complement = target - currentNum;
                TrackedVariable<Integer> complementVar = new TrackedVariable<>("target - nums[i]", complement);
                setLastStepLine(collector, 13);

                // Line 13: if check
                boolean found = map.containsKey(complement);
                setLastStepLine(collector, 13);

                if (found) {
                    // Line 14
                    int j = map.get(complement);
                    setLastStepLine(collector, 14);

                    ans.set(0, j);
                    setLastStepLine(collector, 14);

                    // Line 15
                    ans.set(1, idx);
                    setLastStepLine(collector, 15);

                    numsArr.highlight(j, "FOUND");
                    setLastStepLine(collector, 14);
                    numsArr.highlight(idx, "FOUND");
                    setLastStepLine(collector, 15);

                    result = new int[] { j, idx };

                    // Line 16: return
                    collector.emit(
                            new Step(0, StepAction.RESULT, "Found! ans = [" + j + ", " + idx + "]")
                                    .withValue(result)
                                    .withHighlight("RESULT")
                                    .withLineNumber(16));
                    break;
                }

                // Line 18: map.put
                map.put(currentNum, idx);
                setLastStepLine(collector, 18);
            }

            collector.stopRecording();
            List<Step> steps = collector.getSteps();
            long executionTime = System.currentTimeMillis() - startTime;

            StepCollector.reset();

            return ExecutionResponse.success(
                    "Q0001_TwoSum",
                    "Two Sum",
                    steps,
                    result != null ? result : new int[] {},
                    executionTime);

        } catch (Exception e) {
            log.error("Error executing Two Sum", e);
            StepCollector.reset();
            return ExecutionResponse.error("Q0001_TwoSum", "RUNTIME", e.getMessage(), -1);
        }
    }

    /**
     * Execute sliding window for "Longest Substring Without Repeating Characters"
     * Line numbers map to LongestSubstringWithoutRepeatingCharacters.java:
     * Line 7: method signature
     * Line 8: HashSet<Character> set = new HashSet<>()
     * Line 9: int ans = 0
     * Line 11: int p2 = 0
     * Line 13: for (int i = 0; ...)
     * Line 14: char ch = s.charAt(i)
     * Line 15: if (!set.contains(ch))
     * Line 18: set.add(ch)
     * Line 20: ans = Math.max(ans, i - p2 + 1)
     * Line 23: while (s.charAt(p2) != ch)
     * Line 24: set.remove(s.charAt(p2))
     * Line 25: p2++
     * Line 28: set.remove(s.charAt(p2))
     * Line 29: p2++
     * Line 30: set.add(ch)
     * Line 34: return ans
     */
    public ExecutionResponse executeLongestSubstring(String s) {
        long startTime = System.currentTimeMillis();

        try {
            StepCollector collector = StepCollector.getInstance();
            collector.startRecording();

            // Line 7: method signature - input
            TrackedCharArray chars = new TrackedCharArray("s", s);
            setLastStepLine(collector, 7);

            // Line 8: HashSet<Character> set
            TrackedSet<Character> set = new TrackedSet<>("set");
            setLastStepLine(collector, 8);

            // Line 9: int ans = 0
            TrackedVariable<Integer> ans = new TrackedVariable<>("ans", 0);
            setLastStepLine(collector, 9);

            // Line 11: int p2 = 0
            TrackedVariable<Integer> p2 = new TrackedVariable<>("p2", 0, "s");
            setLastStepLine(collector, 11);

            // Line 13: for loop
            for (int i = 0; i < chars.length(); i++) {
                TrackedVariable<Integer> iVar = new TrackedVariable<>("i", i, "s");
                setLastStepLine(collector, 13);

                // Line 14: char ch = s.charAt(i)
                char ch = chars.get(i);
                setLastStepLine(collector, 14);

                chars.highlight(i, "POINTER_RIGHT");
                setLastStepLine(collector, 14);

                // Line 15: if (!set.contains(ch))
                boolean notContains = !set.contains(ch);
                setLastStepLine(collector, 15);

                if (notContains) {
                    // Line 18: set.add(ch)
                    set.add(ch);
                    setLastStepLine(collector, 18);

                    int currentLen = i - p2.getInt() + 1;
                    if (currentLen > ans.getInt()) {
                        // Line 20: ans = Math.max(...)
                        ans.set(currentLen);
                        setLastStepLine(collector, 20);

                        chars.highlightRange(p2.getInt(), i, "FOUND");
                        setLastStepLine(collector, 20);
                    }
                } else {
                    // Line 23: while loop to shrink
                    while (s.charAt(p2.getInt()) != ch) {
                        // Line 24: set.remove
                        set.remove(s.charAt(p2.getInt()));
                        setLastStepLine(collector, 24);

                        // Line 25: p2++
                        p2.increment();
                        setLastStepLine(collector, 25);
                    }

                    // Line 28: set.remove the duplicate
                    set.remove(s.charAt(p2.getInt()));
                    setLastStepLine(collector, 28);

                    // Line 29: p2++
                    p2.increment();
                    setLastStepLine(collector, 29);

                    // Line 30: set.add(ch)
                    set.add(ch);
                    setLastStepLine(collector, 30);
                }
            }

            // Line 34: return ans
            collector.emit(
                    new Step(0, StepAction.RESULT, "Longest substring length: " + ans.getInt())
                            .withValue(ans.getInt())
                            .withHighlight("RESULT")
                            .withLineNumber(34));

            collector.stopRecording();
            List<Step> steps = collector.getSteps();
            long executionTime = System.currentTimeMillis() - startTime;

            StepCollector.reset();

            return ExecutionResponse.success(
                    "Q0003_LongestSubstringWithoutRepeatingCharacters",
                    "Longest Substring Without Repeating Characters",
                    steps,
                    ans.getInt(),
                    executionTime);

        } catch (Exception e) {
            log.error("Error executing Longest Substring", e);
            StepCollector.reset();
            return ExecutionResponse.error(
                    "Q0003_LongestSubstringWithoutRepeatingCharacters",
                    "RUNTIME",
                    e.getMessage(),
                    -1);
        }
    }

    /**
     * Helper to set line number on the last emitted step
     */
    private void setLastStepLine(StepCollector collector, int lineNumber) {
        List<Step> steps = collector.getSteps();
        if (!steps.isEmpty()) {
            steps.get(steps.size() - 1).setLineNumber(lineNumber);
        }
    }

    /**
     * Generic execution with timeout
     */
    public ExecutionResponse executeWithTimeout(Callable<ExecutionResponse> task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<ExecutionResponse> future = executor.submit(task);
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return ExecutionResponse.error(null, "TIMEOUT",
                    "Execution exceeded " + TIMEOUT_SECONDS + " seconds", -1);
        } catch (Exception e) {
            return ExecutionResponse.error(null, "RUNTIME", e.getMessage(), -1);
        } finally {
            executor.shutdownNow();
        }
    }
}
