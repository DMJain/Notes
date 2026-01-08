package com.visualizer.service;

import com.visualizer.dto.ExecutionResponse;
import com.visualizer.instrumentation.transformer.CodeTransformer;
import com.visualizer.instrumentation.transformer.DynamicCompiler;
import com.visualizer.instrumentation.transformer.InstrumentedRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.visualizer.dto.MethodSignature;
import com.visualizer.dto.ParameterInfo;

/**
 * Service that orchestrates automatic instrumentation and execution.
 * 
 * Flow:
 * 1. Read source code from file
 * 2. Transform using CodeTransformer (JavaParser AST)
 * 3. Compile dynamically using DynamicCompiler
 * 4. Execute using InstrumentedRunner
 * 5. Return collected steps
 */
@Service
public class AutoInstrumentationService {

    private static final Logger log = LoggerFactory.getLogger(AutoInstrumentationService.class);

    private final FileService fileService;
    private final CodeTransformer codeTransformer;
    private final DynamicCompiler dynamicCompiler;
    private final InstrumentedRunner instrumentedRunner;

    public AutoInstrumentationService(FileService fileService,
            CodeTransformer codeTransformer,
            DynamicCompiler dynamicCompiler,
            InstrumentedRunner instrumentedRunner) {
        this.fileService = fileService;
        this.codeTransformer = codeTransformer;
        this.dynamicCompiler = dynamicCompiler;
        this.instrumentedRunner = instrumentedRunner;
    }

    /**
     * Execute a question's solution with automatic instrumentation.
     *
     * @param questionId The question folder name (e.g., "Q0001_TwoSum")
     * @param methodName The method to execute (e.g., "twoSum")
     * @param inputs     The input arguments as a map
     * @return ExecutionResponse with visualization steps
     */
    public ExecutionResponse executeWithAutoInstrumentation(String questionId,
            String methodName,
            Map<String, Object> inputs) {
        log.info("Starting auto-instrumentation for {}.{}", questionId, methodName);

        try {
            // Step 1: Get the original source code
            String sourceCode = fileService.getSolutionCode(questionId);
            if (sourceCode == null || sourceCode.isEmpty()) {
                return ExecutionResponse.error(questionId, "FILE_NOT_FOUND",
                        "Solution code not found for: " + questionId, -1);
            }

            log.debug("Original source code ({} chars):\n{}", sourceCode.length(),
                    sourceCode.substring(0, Math.min(200, sourceCode.length())) + "...");

            // Step 2: Transform the source code
            CodeTransformer.TransformResult transformResult = codeTransformer.transform(sourceCode, methodName);

            if (!transformResult.isSuccess()) {
                return ExecutionResponse.error(questionId, "TRANSFORM_ERROR",
                        transformResult.getError(), -1);
            }

            String transformedCode = transformResult.getTransformedCode();
            log.debug("Transformed code:\n{}",
                    transformedCode.substring(0, Math.min(300, transformedCode.length())) + "...");

            // Step 3: Extract class name from source
            String className = extractClassName(sourceCode);
            if (className == null) {
                return ExecutionResponse.error(questionId, "PARSE_ERROR",
                        "Could not extract class name", -1);
            }

            // Step 4: Compile the transformed code
            DynamicCompiler.CompileResult compileResult = dynamicCompiler.compile(className, transformedCode);

            if (!compileResult.isSuccess()) {
                return ExecutionResponse.error(questionId, "COMPILE_ERROR",
                        compileResult.getError(), -1);
            }

            // Step 5: Prepare arguments from inputs map
            Object[] args = prepareArguments(transformResult.getParameters(), inputs);

            // Step 6: Execute and collect steps
            ExecutionResponse response = instrumentedRunner.execute(compileResult.getCompiledClass(), methodName, args);

            // Add input data to response for frontend display
            response.setInput(inputs);

            return response;

        } catch (Exception e) {
            log.error("Auto-instrumentation failed for {}", questionId, e);
            return ExecutionResponse.error(questionId, "RUNTIME", e.getMessage(), -1);
        }
    }

    /**
     * Get the method signature for a question to build the frontend form.
     */
    public MethodSignature getMethodSignature(String questionId) {
        String sourceCode = fileService.getSolutionCode(questionId);
        if (sourceCode == null || sourceCode.isEmpty()) {
            throw new RuntimeException("Solution code not found for: " + questionId);
        }

        String methodName = extractSolutionMethodName(sourceCode);
        if (methodName == null) {
            throw new RuntimeException("Could not find solution method for: " + questionId);
        }

        List<ParameterInfo> parameters = extractParameters(sourceCode, methodName);
        // Return type is not strictly needed for input form, but good to have.
        // For now, we'll extract it or default to "void" if regex is too complex.
        String returnType = extractReturnType(sourceCode, methodName);

        return new MethodSignature(methodName, returnType, parameters);
    }

    /**
     * Execute demo with auto-instrumentation - automatically detects method name
     * and generates default inputs based on method signature.
     */
    public ExecutionResponse executeDemoWithAutoInstrumentation(String questionId) {
        log.info("Starting auto-demo for {}", questionId);

        try {
            // Get source code
            String sourceCode = fileService.getSolutionCode(questionId);
            if (sourceCode == null || sourceCode.isEmpty()) {
                return ExecutionResponse.error(questionId, "FILE_NOT_FOUND",
                        "Solution code not found for: " + questionId, -1);
            }

            // Auto-detect the main solution method (first non-main public method)
            String methodName = extractSolutionMethodName(sourceCode);
            if (methodName == null) {
                return ExecutionResponse.error(questionId, "PARSE_ERROR",
                        "Could not find solution method", -1);
            }

            log.info("Auto-detected method: {}", methodName);

            // Generate default inputs based on method signature
            Map<String, Object> inputs = generateDefaultInputs(sourceCode, methodName);
            log.info("Generated default inputs: {}", inputs);

            // Execute the transformation pipeline
            return executeWithAutoInstrumentation(questionId, methodName, inputs);

        } catch (Exception e) {
            log.error("Auto-demo failed for {}", questionId, e);
            return ExecutionResponse.error(questionId, "RUNTIME", e.getMessage(), -1);
        }
    }

    /**
     * Extract the main solution method name (first public method that's not
     * 'main').
     */
    private String extractSolutionMethodName(String sourceCode) {
        // Pattern to find method declarations: public/private/static type methodName(
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "public\\s+(?:static\\s+)?\\w+(?:<[^>]+>)?(?:\\[\\])?\\s+(\\w+)\\s*\\(");
        java.util.regex.Matcher matcher = pattern.matcher(sourceCode);

        while (matcher.find()) {
            String methodName = matcher.group(1);
            // Skip main method and constructor-like names (same as class)
            if (!methodName.equals("main")) {
                return methodName;
            }
        }
        return null;
    }

    /**
     * Generate default inputs based on method signature.
     */
    private Map<String, Object> generateDefaultInputs(String sourceCode, String methodName) {
        Map<String, Object> inputs = new java.util.HashMap<>();
        List<ParameterInfo> params = extractParameters(sourceCode, methodName);

        for (ParameterInfo param : params) {
            Object defaultValue = generateDefaultValue(param.getType());
            inputs.put(param.getName(), defaultValue);
        }

        return inputs;
    }

    private List<ParameterInfo> extractParameters(String sourceCode, String methodName) {
        List<ParameterInfo> parameters = new ArrayList<>();

        // Find the method signature
        // Matches: methodName(paramType paramName, ...)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                methodName + "\\s*\\(([^)]*)\\)");
        java.util.regex.Matcher matcher = pattern.matcher(sourceCode);

        if (matcher.find()) {
            String paramsStr = matcher.group(1);
            if (paramsStr.trim().isEmpty()) {
                return parameters;
            }

            String[] paramList = paramsStr.split(",");

            for (String param : paramList) {
                param = param.trim();
                // Split by whitespace to get type and name
                // e.g. "int[] nums" -> ["int[]", "nums"]
                // e.g. "List<String> list" -> ["List<String>", "list"]
                String[] parts = param.split("\\s+");
                if (parts.length >= 2) {
                    String type = parts[parts.length - 2];
                    String name = parts[parts.length - 1];
                    parameters.add(new ParameterInfo(name, type));
                }
            }
        }
        return parameters;
    }

    private String extractReturnType(String sourceCode, String methodName) {
        // Pattern: public [static] ReturnType methodName(
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "public\\s+(?:static\\s+)?(\\S+(?:<[^>]+>)?(?:\\[\\])?)\\s+" + methodName + "\\s*\\(");
        java.util.regex.Matcher matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "void";
    }

    /**
     * Generate a sensible default value for common types
     */
    private Object generateDefaultValue(String type) {
        return switch (type) {
            case "int[]" -> java.util.List.of(2, 7, 11, 15); // Default array
            case "int" -> 9; // Default int
            case "String" -> "abcabcbb"; // Default string
            case "char[]" -> "abcabcbb"; // Default char array
            case "boolean" -> false;
            case "double" -> 0.0;
            case "long" -> 0L;
            default -> null;
        };
    }

    /**
     * Extract fully qualified class name from source code (package.ClassName).
     */
    private String extractClassName(String sourceCode) {
        String packageName = null;
        String className = null;

        // Extract package
        java.util.regex.Pattern packagePattern = java.util.regex.Pattern.compile("package\\s+([\\w.]+);");
        java.util.regex.Matcher packageMatcher = packagePattern.matcher(sourceCode);
        if (packageMatcher.find()) {
            packageName = packageMatcher.group(1);
        }

        // Extract class name
        java.util.regex.Pattern classPattern = java.util.regex.Pattern.compile("class\\s+(\\w+)");
        java.util.regex.Matcher classMatcher = classPattern.matcher(sourceCode);
        if (classMatcher.find()) {
            className = classMatcher.group(1);
        }

        if (className == null) {
            return null;
        }

        // Return fully qualified name
        if (packageName != null) {
            return packageName + "." + className;
        }
        return className;
    }

    /**
     * Prepare method arguments from the inputs map.
     */
    private Object[] prepareArguments(java.util.List<CodeTransformer.ParameterInfo> params,
            Map<String, Object> inputs) {
        Object[] args = new Object[params.size()];

        for (int i = 0; i < params.size(); i++) {
            CodeTransformer.ParameterInfo param = params.get(i);
            Object value = inputs.get(param.getName());

            if (value == null) {
                log.warn("Missing input for parameter: {}", param.getName());
                continue;
            }

            // Convert value to appropriate type
            args[i] = convertValue(value, param.getType());
        }

        return args;
    }

    /**
     * Convert input value to the expected type.
     */
    @SuppressWarnings("unchecked")
    private Object convertValue(Object value, String type) {
        if (type.equals("int[]")) {
            if (value instanceof java.util.List) {
                java.util.List<Number> list = (java.util.List<Number>) value;
                int[] arr = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    arr[i] = list.get(i).intValue();
                }
                return arr;
            }
        }

        if (type.equals("int")) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
        }

        if (type.equals("String")) {
            return value.toString();
        }

        return value;
    }
}
