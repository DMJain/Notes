package com.visualizer.service;

import com.visualizer.config.PathConfig;
import com.visualizer.dto.QuestionDetail;
import com.visualizer.dto.QuestionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final PathConfig pathConfig;

    // Pattern to extract question number and name from folder like "Q0001_TwoSum"
    private static final Pattern QUESTION_PATTERN = Pattern.compile("Q(\\d+)_(.+)");

    public FileService(PathConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    /**
     * List all LeetCode questions from the directory
     */
    public List<QuestionSummary> listLeetCodeQuestions() {
        return listQuestions(pathConfig.getLeetcodeAbsolutePath(), "LEETCODE");
    }

    /**
     * List all LLD topics from the directory
     */
    public List<QuestionSummary> listLldTopics() {
        return listQuestions(pathConfig.getLldAbsolutePath(), "LLD");
    }

    /**
     * Get detailed information about a specific question
     */
    public QuestionDetail getQuestionDetail(String questionId, String category) {
        Path basePath = "LEETCODE".equals(category)
                ? pathConfig.getLeetcodeAbsolutePath()
                : pathConfig.getLldAbsolutePath();

        Path questionDir = basePath.resolve(questionId);

        if (!Files.exists(questionDir) || !Files.isDirectory(questionDir)) {
            throw new IllegalArgumentException("Question not found: " + questionId);
        }

        Matcher matcher = QUESTION_PATTERN.matcher(questionId);
        String number = "";
        String name = questionId;

        if (matcher.matches()) {
            number = matcher.group(1);
            name = camelCaseToSpaces(matcher.group(2));
        }

        return QuestionDetail.builder()
                .id(questionId)
                .number(number)
                .name(name)
                .category(category)
                .questionMarkdown(readFileContent(questionDir.resolve("Question.md")))
                .explanationMarkdown(readFileContent(questionDir.resolve("Explanation.md")))
                .solutionCode(readJavaFile(questionDir))
                .solutionFileName(findJavaFileName(questionDir))
                .build();
    }

    /**
     * Get raw Java solution code for a question (for auto-instrumentation)
     */
    public String getSolutionCode(String questionId) {
        Path basePath = pathConfig.getLeetcodeAbsolutePath();
        Path questionDir = basePath.resolve(questionId);

        if (!Files.exists(questionDir) || !Files.isDirectory(questionDir)) {
            log.warn("Question directory not found: {}", questionDir);
            return null;
        }

        return readJavaFile(questionDir);
    }

    private List<QuestionSummary> listQuestions(Path basePath, String category) {
        log.debug("Listing questions from: {}", basePath);

        if (!Files.exists(basePath)) {
            log.warn("Path does not exist: {}", basePath);
            return List.of();
        }

        try (Stream<Path> paths = Files.list(basePath)) {
            return paths
                    .filter(Files::isDirectory)
                    .filter(p -> QUESTION_PATTERN.matcher(p.getFileName().toString()).matches())
                    .map(p -> toQuestionSummary(p, category))
                    .sorted(Comparator.comparing(QuestionSummary::getNumber))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error listing questions", e);
            return List.of();
        }
    }

    private QuestionSummary toQuestionSummary(Path dir, String category) {
        String folderName = dir.getFileName().toString();
        Matcher matcher = QUESTION_PATTERN.matcher(folderName);

        if (matcher.matches()) {
            return QuestionSummary.builder()
                    .id(folderName)
                    .number(matcher.group(1))
                    .name(camelCaseToSpaces(matcher.group(2)))
                    .category(category)
                    .build();
        }

        return QuestionSummary.builder()
                .id(folderName)
                .number("")
                .name(folderName)
                .category(category)
                .build();
    }

    private String readFileContent(Path filePath) {
        if (!Files.exists(filePath)) {
            return "";
        }
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
            return "";
        }
    }

    private String readJavaFile(Path dir) {
        try (Stream<Path> paths = Files.list(dir)) {
            return paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .findFirst()
                    .map(this::readFileContent)
                    .orElse("");
        } catch (IOException e) {
            log.error("Error finding Java file in: {}", dir, e);
            return "";
        }
    }

    private String findJavaFileName(Path dir) {
        try (Stream<Path> paths = Files.list(dir)) {
            return paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .findFirst()
                    .map(p -> p.getFileName().toString())
                    .orElse("");
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Convert "TwoSum" to "Two Sum"
     */
    private String camelCaseToSpaces(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1 $2");
    }
}
