package com.visualizer.controller;

import com.visualizer.dto.QuestionDetail;
import com.visualizer.dto.QuestionSummary;
import com.visualizer.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final FileService fileService;

    public QuestionController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * GET /api/questions?category=LEETCODE
     * List all questions for a category
     */
    @GetMapping
    public ResponseEntity<List<QuestionSummary>> listQuestions(
            @RequestParam(defaultValue = "LEETCODE") String category) {

        List<QuestionSummary> questions = switch (category.toUpperCase()) {
            case "LEETCODE" -> fileService.listLeetCodeQuestions();
            case "LLD" -> fileService.listLldTopics();
            default -> List.of();
        };

        return ResponseEntity.ok(questions);
    }

    /**
     * GET /api/questions/{questionId}?category=LEETCODE
     * Get detailed information about a specific question
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDetail> getQuestionDetail(
            @PathVariable String questionId,
            @RequestParam(defaultValue = "LEETCODE") String category) {

        try {
            QuestionDetail detail = fileService.getQuestionDetail(questionId, category.toUpperCase());
            return ResponseEntity.ok(detail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
