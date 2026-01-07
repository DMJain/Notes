package com.visualizer.instrumentation.transformer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Transforms Java source code using annotation-guided instrumentation.
 * 
 * Annotations:
 * - @viz:input - Wrap all method parameters
 * - @viz:output - Mark output variable
 * - @viz:loop(var,array) - Track loop counter as pointer to array
 * - @viz:var(name) - Emit variable step
 * - @viz:highlight(array,index) - Emit highlight step
 * - @viz:result(msg) - Emit RESULT step
 */
@Component
public class CodeTransformer {

    private static final Logger log = LoggerFactory.getLogger(CodeTransformer.class);

    // Tracked variable names
    private final Set<String> trackedArrays = new HashSet<>();
    private final Set<String> trackedMaps = new HashSet<>();
    private final Set<String> trackedSets = new HashSet<>();
    private final Set<String> inputParams = new HashSet<>();
    private final Map<String, String> loopVars = new HashMap<>(); // var -> target array

    public TransformResult transform(String sourceCode, String methodName) {
        trackedArrays.clear();
        trackedMaps.clear();
        trackedSets.clear();
        inputParams.clear();
        loopVars.clear();

        // STEP 1: Parse annotations from source (before AST parsing)
        parseAnnotations(sourceCode, methodName);

        log.info("Annotations: inputs={}, arrays={}, maps={}, loops={}",
                inputParams, trackedArrays, trackedMaps, loopVars);

        // STEP 2: Pre-process source code - inject annotation-driven code
        String preprocessedCode = preprocessAnnotations(sourceCode);

        // STEP 3: Parse the preprocessed code
        JavaParser parser = new JavaParser();
        ParseResult<CompilationUnit> parseResult = parser.parse(preprocessedCode);

        if (!parseResult.isSuccessful()) {
            log.error("Failed to parse: {}", parseResult.getProblems());
            return TransformResult.error("Parse failed: " + parseResult.getProblems());
        }

        CompilationUnit cu = parseResult.getResult().orElseThrow();

        Optional<MethodDeclaration> methodOpt = cu.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.getNameAsString().equals(methodName))
                .findFirst();

        if (methodOpt.isEmpty()) {
            return TransformResult.error("Method not found: " + methodName);
        }

        MethodDeclaration targetMethod = methodOpt.get();
        List<ParameterInfo> parameters = extractParameters(targetMethod);

        // AUTO-DETECTION: If no inputs explicitly marked, mark all parameters
        if (inputParams.isEmpty()) {
            for (Parameter param : targetMethod.getParameters()) {
                inputParams.add(param.getNameAsString());
            }
            log.info("Auto-detected inputs: {}", inputParams);
        }

        // AUTO-DETECTION: Find all int[] arrays in the method body
        targetMethod.findAll(VariableDeclarator.class).forEach(var -> {
            if (var.getType() instanceof ArrayType) {
                ArrayType at = (ArrayType) var.getType();
                if (at.getComponentType().asString().equals("int")) {
                    trackedArrays.add(var.getNameAsString());
                }
            }
        });
        log.info("Auto-detected arrays: {}", trackedArrays);

        // STEP 4: Transform AST
        cu.accept(new TransformVisitor(methodName), null);

        // STEP 5: Inject wrapper statements
        injectWrapperStatements(targetMethod);

        // STEP 6: Inject line tracking for every statement
        injectLineTracking(targetMethod);

        addImports(cu);

        String transformedCode = cu.toString();
        log.info("Transformation complete.");

        return TransformResult.success(transformedCode, parameters);
    }

    /**
     * Parse annotations from source code.
     */
    private void parseAnnotations(String sourceCode, String methodName) {
        String[] lines = sourceCode.split("\n");

        for (String line : lines) {
            // @viz:input - mark all params for wrapping
            if (line.contains("@viz:input") && line.contains(methodName + "(")) {
                // Will handle params in injectWrapperStatements
                Matcher paramMatcher = Pattern.compile("(\\w+)\\s*,|\\)").matcher(line);
                int parenStart = line.indexOf('(');
                if (parenStart > 0) {
                    String params = line.substring(parenStart);
                    // Extract param names
                    Matcher m = Pattern.compile("(\\w+)\\s*[,)]").matcher(params);
                    while (m.find()) {
                        String paramName = m.group(1);
                        // Skip types
                        if (!paramName.matches("int|String|boolean|long|double|float|char")) {
                            inputParams.add(paramName);
                        }
                    }
                    // Also check for array params
                    if (line.contains("int[]")) {
                        Matcher arrM = Pattern.compile("int\\[\\]\\s+(\\w+)").matcher(line);
                        while (arrM.find()) {
                            trackedArrays.add(arrM.group(1));
                            inputParams.add(arrM.group(1));
                        }
                    }
                }
            }

            // @viz:output
            if (line.contains("@viz:output")) {
                Matcher varMatcher = Pattern.compile("(\\w+)\\s*=").matcher(line);
                if (varMatcher.find()) {
                    trackedArrays.add(varMatcher.group(1));
                }
            }

            // @viz:loop(var,array)
            if (line.contains("@viz:loop")) {
                Matcher loopMatcher = Pattern.compile("@viz:loop\\((\\w+),(\\w+)\\)").matcher(line);
                if (loopMatcher.find()) {
                    loopVars.put(loopMatcher.group(1), loopMatcher.group(2));
                }
            }
        }

        // Auto-detect HashMap/HashSet - require angle brackets to avoid matching
        // comments
        Matcher mapMatcher = Pattern.compile("HashMap<[^>]+>\\s+(\\w+)\\s*=").matcher(sourceCode);
        while (mapMatcher.find()) {
            trackedMaps.add(mapMatcher.group(1));
        }
        Matcher setMatcher = Pattern.compile("HashSet<[^>]+>\\s+(\\w+)\\s*=").matcher(sourceCode);
        while (setMatcher.find()) {
            trackedSets.add(setMatcher.group(1));
        }
    }

    /**
     * Pre-process source code to inject annotation-driven code.
     */
    private String preprocessAnnotations(String sourceCode) {
        StringBuilder result = new StringBuilder();
        String[] lines = sourceCode.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();
            int sourceLineNumber = i + 1; // 1-indexed line number

            // Handle @viz:loop - inject TrackedVariable after for-loop opening brace
            if (line.contains("@viz:loop")) {
                Matcher m = Pattern.compile("@viz:loop\\((\\w+),(\\w+)\\)").matcher(line);
                if (m.find()) {
                    String varName = m.group(1);
                    String targetArray = m.group(2);
                    // Remove the annotation from the line
                    line = line.replaceFirst("//\\s*@viz:loop\\([^)]+\\)", "");
                    result.append(line).append("\n");
                    // Inject line number and TrackedVariable creation
                    result.append("            StepCollector.getInstance().setCurrentLine(").append(sourceLineNumber)
                            .append(");\n");
                    result.append("            TrackedVariable<Integer> _").append(varName)
                            .append(" = new TrackedVariable<>(\"").append(varName).append("\", ")
                            .append(varName).append(", \"").append(targetArray).append("\");\n");
                    continue;
                }
            }

            // Handle @viz:var(name) - inject TrackedVariable
            if (line.contains("@viz:var")) {
                Matcher m = Pattern.compile("@viz:var\\(([^)]+)\\)").matcher(line);
                if (m.find()) {
                    String displayName = m.group(1);
                    // Extract variable being assigned
                    Matcher varM = Pattern.compile("(\\w+)\\s*=\\s*(.+?)\\s*;").matcher(line);
                    if (varM.find()) {
                        String varName = varM.group(1);
                        String expr = varM.group(2);
                        line = line.replaceFirst("//\\s*@viz:var\\([^)]+\\)", "");
                        result.append(line).append("\n");
                        result.append("            StepCollector.getInstance().setCurrentLine(")
                                .append(sourceLineNumber).append(");\n");
                        result.append("            new TrackedVariable<>(\"").append(displayName)
                                .append("\", ").append(varName).append(");\n");
                        continue;
                    }
                }
            }

            // Handle @viz:highlight(array,index)
            if (line.contains("@viz:highlight")) {
                Matcher m = Pattern.compile("@viz:highlight\\((\\w+),(\\w+(?:\\[\\d+\\])?)\\)").matcher(line);
                if (m.find()) {
                    String arrayName = m.group(1);
                    String indexExpr = m.group(2);
                    // Remove just the annotation comment
                    line = line.replaceFirst("//\\s*@viz:highlight\\([^)]+\\)", "");
                    result.append(line).append("\n");
                    result.append("            StepCollector.getInstance().setCurrentLine(").append(sourceLineNumber)
                            .append(");\n");
                    // Use _arrayName for input params, arrayName for local arrays
                    String targetName = inputParams.contains(arrayName) ? "_" + arrayName : arrayName;
                    result.append("            ").append(targetName).append(".highlight(")
                            .append(indexExpr).append(", \"FOUND\");\n");
                    continue;
                }
            }

            // Handle @viz:result(msg)
            if (line.contains("@viz:result")) {
                Matcher m = Pattern.compile("@viz:result\\(([^)]+)\\)").matcher(line);
                if (m.find()) {
                    String msg = m.group(1);
                    line = line.replaceFirst("//\\s*@viz:result\\([^)]+\\)", "");
                    // Inject RESULT step before return
                    result.append("            StepCollector.getInstance().emit(new Step(0, StepAction.RESULT, \"")
                            .append(msg).append("\").withLineNumber(").append(sourceLineNumber)
                            .append(").withHighlight(\"RESULT\"));\n");
                    result.append(line).append("\n");
                    continue;
                }
            }

            result.append(line).append("\n");
        }

        return result.toString();
    }

    /**
     * Inject TrackedArray/TrackedVariable wrapper statements at method body start.
     */
    private void injectWrapperStatements(MethodDeclaration method) {
        if (method.getBody().isEmpty())
            return;

        BlockStmt body = method.getBody().get();
        List<Statement> wrapperStmts = new ArrayList<>();

        for (Parameter param : method.getParameters()) {
            String paramName = param.getNameAsString();
            if (!inputParams.contains(paramName))
                continue;

            if (param.getType() instanceof ArrayType) {
                ArrayType at = (ArrayType) param.getType();
                if (at.getComponentType().asString().equals("int")) {
                    String wrapperName = "_" + paramName;
                    Statement stmt = createTrackedArrayWrapper(wrapperName, paramName, paramName);
                    wrapperStmts.add(stmt);
                }
            } else if (param.getType() instanceof PrimitiveType) {
                PrimitiveType pt = (PrimitiveType) param.getType();
                if (pt.getType() == PrimitiveType.Primitive.INT) {
                    String wrapperName = "_" + paramName;
                    Statement stmt = createTrackedVariableWrapper(wrapperName, paramName, paramName);
                    wrapperStmts.add(stmt);
                }
            } else if (param.getType().asString().equals("String")) {
                // Wrap String parameters for pointer visualization
                String wrapperName = "_" + paramName;
                Statement stmt = createTrackedStringWrapper(wrapperName, paramName, paramName);
                wrapperStmts.add(stmt);
            }
        }

        NodeList<Statement> stmts = body.getStatements();
        for (int i = wrapperStmts.size() - 1; i >= 0; i--) {
            stmts.addFirst(wrapperStmts.get(i));
        }
    }

    /**
     * Inject line tracking for every statement in the method body.
     */
    private void injectLineTracking(MethodDeclaration method) {
        if (method.getBody().isEmpty())
            return;

        method.getBody().get().findAll(Statement.class).forEach(stmt -> {
            // Only inject for statements that have a line number and are inside the target
            // method
            if (stmt.getBegin().isPresent()) {
                int line = stmt.getBegin().get().line;
                // Avoid injecting into block statements themselves (only their children)
                if (stmt instanceof BlockStmt)
                    return;

                // Create the tracking call: StepCollector.getInstance().setCurrentLine(line);
                MethodCallExpr call = new MethodCallExpr(
                        new MethodCallExpr(new NameExpr("StepCollector"), "getInstance"),
                        "setCurrentLine",
                        new NodeList<>(new IntegerLiteralExpr(String.valueOf(line))));

                // We can't easily inject *before* every statement without messing up the AST
                // structure (e.g. if/else blocks).
                // Instead, we'll try to insert it into BlockStmts where possible.
                if (stmt.getParentNode().isPresent() && stmt.getParentNode().get() instanceof BlockStmt) {
                    BlockStmt parent = (BlockStmt) stmt.getParentNode().get();
                    int index = parent.getStatements().indexOf(stmt);
                    if (index >= 0) {
                        // Check if we already injected for this line (simple heuristic)
                        boolean alreadyInjected = false;
                        if (index > 0) {
                            Statement prev = parent.getStatements().get(index - 1);
                            if (prev.toString().contains("setCurrentLine(" + line + ")")) {
                                alreadyInjected = true;
                            }
                        }

                        if (!alreadyInjected) {
                            parent.getStatements().add(index, new ExpressionStmt(call));
                        }
                    }
                }
            }
        });
    }

    private Statement createTrackedArrayWrapper(String wrapperName, String displayName, String sourceVar) {
        return new ExpressionStmt(
                new VariableDeclarationExpr(
                        new VariableDeclarator(
                                new ClassOrInterfaceType(null, "TrackedArray"),
                                wrapperName,
                                new ObjectCreationExpr()
                                        .setType("TrackedArray")
                                        .addArgument(new StringLiteralExpr(displayName))
                                        .addArgument(new NameExpr(sourceVar)))));
    }

    private Statement createTrackedVariableWrapper(String wrapperName, String displayName, String sourceVar) {
        return new ExpressionStmt(
                new VariableDeclarationExpr(
                        new VariableDeclarator(
                                new ClassOrInterfaceType(null, "TrackedVariable<Integer>"),
                                wrapperName,
                                new ObjectCreationExpr()
                                        .setType("TrackedVariable<>")
                                        .addArgument(new StringLiteralExpr(displayName))
                                        .addArgument(new NameExpr(sourceVar)))));
    }

    private Statement createTrackedStringWrapper(String wrapperName, String displayName, String sourceVar) {
        return new ExpressionStmt(
                new VariableDeclarationExpr(
                        new VariableDeclarator(
                                new ClassOrInterfaceType(null, "TrackedString"),
                                wrapperName,
                                new ObjectCreationExpr()
                                        .setType("TrackedString")
                                        .addArgument(new StringLiteralExpr(displayName))
                                        .addArgument(new NameExpr(sourceVar)))));
    }

    private List<ParameterInfo> extractParameters(MethodDeclaration method) {
        List<ParameterInfo> params = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            params.add(new ParameterInfo(param.getNameAsString(), param.getTypeAsString()));
        }
        return params;
    }

    private void addImports(CompilationUnit cu) {
        cu.addImport("com.visualizer.instrumentation.linear.TrackedArray");
        cu.addImport("com.visualizer.instrumentation.linear.TrackedCharArray");
        cu.addImport("com.visualizer.instrumentation.linear.TrackedString");
        cu.addImport("com.visualizer.instrumentation.associative.TrackedMap");
        cu.addImport("com.visualizer.instrumentation.associative.TrackedSet");
        cu.addImport("com.visualizer.instrumentation.core.TrackedVariable");
        cu.addImport("com.visualizer.instrumentation.core.StepCollector");
        cu.addImport("com.visualizer.instrumentation.core.Step");
        cu.addImport("com.visualizer.instrumentation.core.StepAction");
    }

    /**
     * Transform AST using visitor.
     */
    private class TransformVisitor extends ModifierVisitor<Void> {
        private final String targetMethodName;
        private boolean inTargetMethod = false;

        public TransformVisitor(String targetMethodName) {
            this.targetMethodName = targetMethodName;
        }

        @Override
        public Visitable visit(MethodDeclaration n, Void arg) {
            if (n.getNameAsString().equals(targetMethodName)) {
                inTargetMethod = true;
                Visitable result = super.visit(n, arg);
                inTargetMethod = false;
                return result;
            }
            return super.visit(n, arg);
        }

        @Override
        public Visitable visit(VariableDeclarator n, Void arg) {
            if (!inTargetMethod)
                return super.visit(n, arg);

            String varName = n.getNameAsString();

            // Transform local int[] → TrackedArray
            if (trackedArrays.contains(varName) && n.getType() instanceof ArrayType && !inputParams.contains(varName)) {
                n.setType(new ClassOrInterfaceType(null, "TrackedArray"));
                n.getInitializer().ifPresent(init -> {
                    if (init instanceof ArrayCreationExpr) {
                        ArrayCreationExpr ace = (ArrayCreationExpr) init;
                        if (!ace.getLevels().isEmpty() && ace.getLevels().get(0).getDimension().isPresent()) {
                            Expression size = ace.getLevels().get(0).getDimension().get();
                            n.setInitializer(new ObjectCreationExpr()
                                    .setType("TrackedArray")
                                    .addArgument(new StringLiteralExpr(varName))
                                    .addArgument(size));
                        }
                    } else if (init instanceof ArrayInitializerExpr) {
                        ArrayInitializerExpr aie = (ArrayInitializerExpr) init;
                        ArrayCreationExpr newArr = new ArrayCreationExpr()
                                .setElementType(new PrimitiveType(PrimitiveType.Primitive.INT))
                                .setInitializer(aie);
                        n.setInitializer(new ObjectCreationExpr()
                                .setType("TrackedArray")
                                .addArgument(new StringLiteralExpr(varName))
                                .addArgument(newArr));
                    }
                });
            }

            // Transform HashMap → TrackedMap
            if (trackedMaps.contains(varName) && n.getType() instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType cit = (ClassOrInterfaceType) n.getType();
                cit.setName("TrackedMap");
                n.getInitializer().ifPresent(init -> {
                    if (init instanceof ObjectCreationExpr) {
                        n.setInitializer(new ObjectCreationExpr()
                                .setType("TrackedMap")
                                .addArgument(new StringLiteralExpr(varName)));
                    }
                });
            }

            // Transform HashSet → TrackedSet
            if (trackedSets.contains(varName) && n.getType() instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType cit = (ClassOrInterfaceType) n.getType();
                cit.setName("TrackedSet");
                n.getInitializer().ifPresent(init -> {
                    if (init instanceof ObjectCreationExpr) {
                        n.setInitializer(new ObjectCreationExpr()
                                .setType("TrackedSet")
                                .addArgument(new StringLiteralExpr(varName)));
                    }
                });
            }

            return super.visit(n, arg);
        }

        @Override
        public Visitable visit(ArrayAccessExpr n, Void arg) {
            if (!inTargetMethod)
                return super.visit(n, arg);

            if (n.getName() instanceof NameExpr) {
                String arrayName = ((NameExpr) n.getName()).getNameAsString();

                if (inputParams.contains(arrayName)) {
                    return new MethodCallExpr(new NameExpr("_" + arrayName), "get",
                            new NodeList<>(n.getIndex()));
                }

                if (trackedArrays.contains(arrayName) && !inputParams.contains(arrayName)) {
                    return new MethodCallExpr(new NameExpr(arrayName), "get",
                            new NodeList<>(n.getIndex()));
                }
            }
            return super.visit(n, arg);
        }

        @Override
        public Visitable visit(MethodCallExpr n, Void arg) {
            if (!inTargetMethod)
                return super.visit(n, arg);

            // Transform String method calls: s.charAt(i) → _s.charAt(i), s.length() →
            // _s.length()
            if (n.getScope().isPresent() && n.getScope().get() instanceof NameExpr) {
                String scopeName = ((NameExpr) n.getScope().get()).getNameAsString();
                String methodName = n.getNameAsString();

                // If this is a String input parameter being called with charAt or length
                if (inputParams.contains(scopeName)) {
                    // Check if this is a String-related method
                    if (methodName.equals("charAt") || methodName.equals("length")) {
                        // Replace scope with wrapped version
                        n.setScope(new NameExpr("_" + scopeName));
                    }
                }
            }
            return super.visit(n, arg);
        }

        @Override
        public Visitable visit(AssignExpr n, Void arg) {
            if (!inTargetMethod)
                return super.visit(n, arg);

            if (n.getTarget() instanceof ArrayAccessExpr) {
                ArrayAccessExpr aae = (ArrayAccessExpr) n.getTarget();
                if (aae.getName() instanceof NameExpr) {
                    String arrayName = ((NameExpr) aae.getName()).getNameAsString();

                    String targetName = null;
                    if (inputParams.contains(arrayName)) {
                        targetName = "_" + arrayName;
                    } else if (trackedArrays.contains(arrayName)) {
                        targetName = arrayName;
                    }

                    if (targetName != null) {
                        return new MethodCallExpr(new NameExpr(targetName), "set")
                                .addArgument(aae.getIndex())
                                .addArgument(n.getValue());
                    }
                }
            }
            return super.visit(n, arg);
        }

        @Override
        public Visitable visit(ReturnStmt n, Void arg) {
            if (!inTargetMethod)
                return super.visit(n, arg);

            n.getExpression().ifPresent(expr -> {
                if (expr instanceof NameExpr) {
                    String varName = ((NameExpr) expr).getNameAsString();
                    if (trackedArrays.contains(varName) && !inputParams.contains(varName)) {
                        n.setExpression(new MethodCallExpr(new NameExpr(varName), "toArray"));
                    }
                }
            });
            return super.visit(n, arg);
        }
    }

    public static class TransformResult {
        private final boolean success;
        private final String transformedCode;
        private final String error;
        private final List<ParameterInfo> parameters;

        private TransformResult(boolean success, String transformedCode, String error, List<ParameterInfo> parameters) {
            this.success = success;
            this.transformedCode = transformedCode;
            this.error = error;
            this.parameters = parameters;
        }

        public static TransformResult success(String code, List<ParameterInfo> params) {
            return new TransformResult(true, code, null, params);
        }

        public static TransformResult error(String error) {
            return new TransformResult(false, null, error, List.of());
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransformedCode() {
            return transformedCode;
        }

        public String getError() {
            return error;
        }

        public List<ParameterInfo> getParameters() {
            return parameters;
        }
    }

    public static class ParameterInfo {
        private final String name;
        private final String type;

        public ParameterInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
