package com.visualizer.dto;

public class QuestionDetail {
    private String id;
    private String number;
    private String name;
    private String category;
    private String questionMarkdown;
    private String explanationMarkdown;
    private String solutionCode;
    private String solutionFileName;

    public QuestionDetail() {
    }

    public QuestionDetail(String id, String number, String name, String category,
            String questionMarkdown, String explanationMarkdown,
            String solutionCode, String solutionFileName) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.category = category;
        this.questionMarkdown = questionMarkdown;
        this.explanationMarkdown = explanationMarkdown;
        this.solutionCode = solutionCode;
        this.solutionFileName = solutionFileName;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionMarkdown() {
        return questionMarkdown;
    }

    public String getExplanationMarkdown() {
        return explanationMarkdown;
    }

    public String getSolutionCode() {
        return solutionCode;
    }

    public String getSolutionFileName() {
        return solutionFileName;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuestionMarkdown(String questionMarkdown) {
        this.questionMarkdown = questionMarkdown;
    }

    public void setExplanationMarkdown(String explanationMarkdown) {
        this.explanationMarkdown = explanationMarkdown;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public void setSolutionFileName(String solutionFileName) {
        this.solutionFileName = solutionFileName;
    }

    // Static builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String number;
        private String name;
        private String category;
        private String questionMarkdown;
        private String explanationMarkdown;
        private String solutionCode;
        private String solutionFileName;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder questionMarkdown(String questionMarkdown) {
            this.questionMarkdown = questionMarkdown;
            return this;
        }

        public Builder explanationMarkdown(String explanationMarkdown) {
            this.explanationMarkdown = explanationMarkdown;
            return this;
        }

        public Builder solutionCode(String solutionCode) {
            this.solutionCode = solutionCode;
            return this;
        }

        public Builder solutionFileName(String solutionFileName) {
            this.solutionFileName = solutionFileName;
            return this;
        }

        public QuestionDetail build() {
            return new QuestionDetail(id, number, name, category, questionMarkdown,
                    explanationMarkdown, solutionCode, solutionFileName);
        }
    }
}
