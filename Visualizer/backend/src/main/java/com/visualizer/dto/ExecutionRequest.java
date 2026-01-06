package com.visualizer.dto;

import java.util.Map;

/**
 * Request to execute a solution with custom input
 */
public class ExecutionRequest {
    private String questionId;
    private String category;
    private Map<String, Object> input;

    public ExecutionRequest() {
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }
}
