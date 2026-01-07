package com.visualizer.controller;

import com.visualizer.dto.ExecutionRequest;
import com.visualizer.dto.ExecutionResponse;
import com.visualizer.service.AutoInstrumentationService;
import com.visualizer.service.ExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/execute")
public class ExecutionController {

    private final ExecutionService executionService;
    private final AutoInstrumentationService autoInstrumentationService;

    public ExecutionController(ExecutionService executionService,
            AutoInstrumentationService autoInstrumentationService) {
        this.executionService = executionService;
        this.autoInstrumentationService = autoInstrumentationService;
    }

    /**
     * POST /api/execute
     * Execute a solution with custom input (legacy hardcoded support)
     */
    @PostMapping
    public ResponseEntity<ExecutionResponse> execute(@RequestBody ExecutionRequest request) {
        String questionId = request.getQuestionId();
        Map<String, Object> input = request.getInput();

        ExecutionResponse response = switch (questionId) {
            case "Q0001_TwoSum" -> {
                int[] nums = parseIntArray(input.get("nums"));
                int target = ((Number) input.get("target")).intValue();
                yield executionService.executeTwoSum(nums, target);
            }
            case "Q0003_LongestSubstringWithoutRepeatingCharacters" -> {
                String s = (String) input.get("s");
                yield executionService.executeLongestSubstring(s);
            }
            default -> ExecutionResponse.error(questionId, "NOT_SUPPORTED",
                    "Visualization not yet implemented for this question", -1);
        };

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/execute/demo/{questionId}
     * Run a demo with default inputs (legacy hardcoded support)
     */
    @GetMapping("/demo/{questionId}")
    public ResponseEntity<ExecutionResponse> demo(@PathVariable String questionId) {
        ExecutionResponse response = switch (questionId) {
            case "Q0001_TwoSum" ->
                executionService.executeTwoSum(new int[] { 2, 7, 11, 15 }, 9);
            case "Q0003_LongestSubstringWithoutRepeatingCharacters" ->
                executionService.executeLongestSubstring("abcabcbb");
            default -> ExecutionResponse.error(questionId, "NOT_SUPPORTED",
                    "Demo not available for this question", -1);
        };

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/execute/auto
     * Execute with auto-instrumentation (no manual coding required)
     */
    @PostMapping("/auto")
    public ResponseEntity<ExecutionResponse> executeAuto(@RequestBody AutoExecutionRequest request) {
        ExecutionResponse response = autoInstrumentationService.executeWithAutoInstrumentation(
                request.getQuestionId(),
                request.getMethodName(),
                request.getInputs());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/execute/auto/demo/{questionId}
     * Execute demo with auto-instrumentation - works for ANY question!
     * Uses default inputs based on method signature.
     */
    @GetMapping("/auto/demo/{questionId}")
    public ResponseEntity<ExecutionResponse> demoAuto(@PathVariable String questionId) {
        // Use the auto-instrumentation service with dynamic method detection
        ExecutionResponse response = autoInstrumentationService.executeDemoWithAutoInstrumentation(questionId);
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("unchecked")
    private int[] parseIntArray(Object obj) {
        if (obj instanceof List) {
            List<Number> list = (List<Number>) obj;
            return list.stream().mapToInt(Number::intValue).toArray();
        }
        return new int[0];
    }

    /**
     * Request for auto-instrumentation execution
     */
    public static class AutoExecutionRequest {
        private String questionId;
        private String methodName;
        private Map<String, Object> inputs;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Map<String, Object> getInputs() {
            return inputs;
        }

        public void setInputs(Map<String, Object> inputs) {
            this.inputs = inputs;
        }
    }
}
