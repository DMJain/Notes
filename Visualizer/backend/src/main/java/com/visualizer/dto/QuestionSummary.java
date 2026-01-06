package com.visualizer.dto;

public class QuestionSummary {
    private String id;
    private String number;
    private String name;
    private String category;

    public QuestionSummary() {
    }

    public QuestionSummary(String id, String number, String name, String category) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.category = category;
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

    // Static builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String number;
        private String name;
        private String category;

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

        public QuestionSummary build() {
            return new QuestionSummary(id, number, name, category);
        }
    }
}
