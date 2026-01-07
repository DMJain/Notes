package com.visualizer.instrumentation.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Compiles Java source code at runtime using javax.tools.JavaCompiler.
 */
@Component
public class DynamicCompiler {

    private static final Logger log = LoggerFactory.getLogger(DynamicCompiler.class);

    /**
     * Compile Java source code and return the compiled class.
     *
     * @param className  Fully qualified class name (e.g., "TwoSum")
     * @param sourceCode The transformed Java source code
     * @return The compiled Class object, or null if compilation failed
     */
    public CompileResult compile(String className, String sourceCode) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            return CompileResult.error("Java compiler not available. Ensure you're running on a JDK, not JRE.");
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        InMemoryFileManager fileManager = new InMemoryFileManager(
                compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8));

        // Create the source file object
        JavaFileObject sourceFile = new InMemorySourceFile(className, sourceCode);

        // Set compilation options - add classpath for our instrumentation classes
        String classpath = System.getProperty("java.class.path");
        List<String> options = Arrays.asList("-classpath", classpath);

        // Compile
        JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, diagnostics, options, null, Collections.singletonList(sourceFile));

        boolean success = task.call();

        if (!success) {
            StringBuilder errors = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                errors.append(String.format("Line %d: %s%n",
                        diagnostic.getLineNumber(),
                        diagnostic.getMessage(null)));
            }
            log.error("Compilation failed: {}", errors);
            return CompileResult.error(errors.toString());
        }

        // Get the compiled class
        try {
            Class<?> clazz = fileManager.getClassLoader(null).loadClass(className);
            log.info("Successfully compiled class: {}", className);
            return CompileResult.success(clazz);
        } catch (ClassNotFoundException e) {
            log.error("Class not found after compilation: {}", className, e);
            return CompileResult.error("Class not found: " + e.getMessage());
        }
    }

    /**
     * Result of compilation.
     */
    public static class CompileResult {
        private final boolean success;
        private final Class<?> compiledClass;
        private final String error;

        private CompileResult(boolean success, Class<?> compiledClass, String error) {
            this.success = success;
            this.compiledClass = compiledClass;
            this.error = error;
        }

        public static CompileResult success(Class<?> clazz) {
            return new CompileResult(true, clazz, null);
        }

        public static CompileResult error(String error) {
            return new CompileResult(false, null, error);
        }

        public boolean isSuccess() {
            return success;
        }

        public Class<?> getCompiledClass() {
            return compiledClass;
        }

        public String getError() {
            return error;
        }
    }

    /**
     * In-memory source file for compilation.
     */
    private static class InMemorySourceFile extends SimpleJavaFileObject {
        private final String sourceCode;

        InMemorySourceFile(String className, String sourceCode) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension),
                    Kind.SOURCE);
            this.sourceCode = sourceCode;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
        }
    }

    /**
     * In-memory class file to store compiled bytecode.
     */
    private static class InMemoryClassFile extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InMemoryClassFile(String className) {
            super(URI.create("mem:///" + className.replace('.', '/') + Kind.CLASS.extension),
                    Kind.CLASS);
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        byte[] getBytes() {
            return outputStream.toByteArray();
        }
    }

    /**
     * File manager that stores compiled classes in memory.
     */
    private static class InMemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, InMemoryClassFile> classFiles = new HashMap<>();

        InMemoryFileManager(StandardJavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                JavaFileObject.Kind kind, FileObject sibling) {
            InMemoryClassFile classFile = new InMemoryClassFile(className);
            classFiles.put(className, classFile);
            return classFile;
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return new ClassLoader(getClass().getClassLoader()) {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    InMemoryClassFile classFile = classFiles.get(name);
                    if (classFile != null) {
                        byte[] bytes = classFile.getBytes();
                        return defineClass(name, bytes, 0, bytes.length);
                    }
                    throw new ClassNotFoundException(name);
                }
            };
        }
    }
}
