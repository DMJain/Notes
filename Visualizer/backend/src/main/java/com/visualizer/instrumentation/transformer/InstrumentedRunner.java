package com.visualizer.instrumentation.transformer;

import com.visualizer.dto.ExecutionResponse;
import com.visualizer.instrumentation.core.Step;
import com.visualizer.instrumentation.core.StepCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Executes instrumented code and collects visualization steps.
 */
@Component
public class InstrumentedRunner {

    private static final Logger log = LoggerFactory.getLogger(InstrumentedRunner.class);

    /**
     * Execute the instrumented method with given inputs.
     *
     * @param clazz      The compiled instrumented class
     * @param methodName The method to execute
     * @param args       The method arguments
     * @return ExecutionResponse with steps
     */
    public ExecutionResponse execute(Class<?> clazz, String methodName, Object... args) {
        long startTime = System.currentTimeMillis();

        try {
            // Start step collection
            StepCollector collector = StepCollector.getInstance();
            collector.startRecording();

            // Find the method
            Method method = findMethod(clazz, methodName, args);
            if (method == null) {
                return ExecutionResponse.error(null, "METHOD_NOT_FOUND",
                        "Method not found: " + methodName, -1);
            }

            // Create instance and invoke
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Object result = method.invoke(instance, args);

            // Stop and collect steps
            collector.stopRecording();
            List<Step> steps = collector.getSteps();
            long executionTime = System.currentTimeMillis() - startTime;

            StepCollector.reset();

            log.info("Execution complete. {} steps collected in {}ms", steps.size(), executionTime);

            return ExecutionResponse.success(
                    clazz.getSimpleName(),
                    methodName,
                    steps,
                    result,
                    executionTime);

        } catch (Exception e) {
            log.error("Execution failed", e);
            StepCollector.reset();
            return ExecutionResponse.error(null, "RUNTIME",
                    e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), -1);
        }
    }

    /**
     * Find the method by name, matching argument types.
     */
    private Method findMethod(Class<?> clazz, String methodName, Object[] args) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == args.length) {
                    boolean match = true;
                    for (int i = 0; i < paramTypes.length; i++) {
                        if (!isCompatible(paramTypes[i], args[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        method.setAccessible(true);
                        return method;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check if an argument is compatible with a parameter type.
     */
    private boolean isCompatible(Class<?> paramType, Object arg) {
        if (arg == null)
            return !paramType.isPrimitive();

        Class<?> argType = arg.getClass();

        // Handle primitives
        if (paramType == int.class)
            return argType == Integer.class;
        if (paramType == long.class)
            return argType == Long.class;
        if (paramType == double.class)
            return argType == Double.class;
        if (paramType == boolean.class)
            return argType == Boolean.class;
        if (paramType == char.class)
            return argType == Character.class;

        // Handle arrays
        if (paramType == int[].class)
            return argType == int[].class;
        if (paramType == String.class)
            return argType == String.class;

        return paramType.isAssignableFrom(argType);
    }
}
