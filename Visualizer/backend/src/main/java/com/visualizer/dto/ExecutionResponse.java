package com.visualizer.dto;

import com.visualizer.instrumentation.core.Step;
import java.util.List;
import java.util.Map;

/**
 * Response from executing a visualizable solution
 */
public class ExecutionResponse {
    private String questionId;
    private String questionName;
    private boolean success;
    private List<Step> steps;
    private int totalSteps;
    private long executionTimeMs;
    private Object result;
    private Map<String, Object> input; // Function arguments
    private Object output; // Return value
    private ErrorInfo error;

    public ExecutionResponse() {
    }

    // Success response
    public static ExecutionResponse success(String questionId, String questionName,
            List<Step> steps, Object result, long executionTimeMs) {
        ExecutionResponse response = new ExecutionResponse();
        response.questionId = questionId;
        response.questionName = questionName;
        response.success = true;
        response.steps = steps;
        response.totalSteps = steps.size();
        response.result = result;
        response.output = result; // Also set output for frontend
        response.executionTimeMs = executionTimeMs;
        return response;
    }

    // Success response with input
    public static ExecutionResponse success(String questionId, String questionName,
            List<Step> steps, Object result, long executionTimeMs, Map<String, Object> input) {
        ExecutionResponse response = success(questionId, questionName, steps, result, executionTimeMs);
        response.input = input;
        return response;
    }

    // Error response
    public static ExecutionResponse error(String questionId, String errorType,
            String message, int lineNumber) {
        ExecutionResponse response = new ExecutionResponse();
        response.questionId = questionId;
        response.success = false;
        response.error = new ErrorInfo(errorType, message, lineNumber);
        return response;
    }

    // Getters and setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public static class ErrorInfo {
        private String type;
        private String message;
        private int lineNumber;

        public ErrorInfo() {
        }

        public ErrorInfo(String type, String message, int lineNumber) {
            this.type = type;
            this.message = message;
            this.lineNumber = lineNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
