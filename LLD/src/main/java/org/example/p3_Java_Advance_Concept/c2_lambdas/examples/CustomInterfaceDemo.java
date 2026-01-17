package org.example.p3_Java_Advance_Concept.c2_lambdas.examples;

import org.example.p3_Java_Advance_Concept.c2_lambdas.interfaces.*;

/**
 * Demonstrates usage of custom functional interfaces.
 */
public class CustomInterfaceDemo {

    /**
     * Demonstrates MathOperation interface.
     */
    public static void demoMathOperation() {
        System.out.println("   🔹 MathOperation examples:\n");

        MathOperation add = (a, b) -> a + b;
        MathOperation subtract = (a, b) -> a - b;
        MathOperation multiply = (a, b) -> a * b;
        MathOperation power = (a, b) -> (int) Math.pow(a, b);

        System.out.println("      add.operate(10, 5) = " + add.operate(10, 5));
        System.out.println("      subtract.operate(10, 5) = " + subtract.operate(10, 5));
        System.out.println("      multiply.operate(10, 5) = " + multiply.operate(10, 5));
        System.out.println("      power.operate(2, 8) = " + power.operate(2, 8));

        // Passing lambda to a method
        System.out.println("\n      Passing to calculate method:");
        System.out.println("      calculate(100, 25, add) = " + calculate(100, 25, add));
        System.out.println("      calculate(100, 25, modulo) = " + calculate(100, 25, (a, b) -> a % b));
    }

    /**
     * Helper method that accepts MathOperation.
     */
    private static int calculate(int a, int b, MathOperation op) {
        return op.operate(a, b);
    }

    /**
     * Demonstrates StringProcessor interface.
     */
    public static void demoStringProcessor() {
        System.out.println("\n   🔹 StringProcessor examples:\n");

        StringProcessor toUpper = s -> s.toUpperCase();
        StringProcessor reverse = s -> new StringBuilder(s).reverse().toString();
        StringProcessor removeVowels = s -> s.replaceAll("[aeiouAEIOU]", "");

        String test = "Hello Lambda";
        System.out.println("      Original: " + test);
        System.out.println("      toUpper: " + toUpper.process(test));
        System.out.println("      reverse: " + reverse.process(test));
        System.out.println("      noVowels: " + removeVowels.process(test));
    }

    /**
     * Demonstrates Validator interface with chaining.
     */
    public static void demoValidator() {
        System.out.println("\n   🔹 Validator with chaining:\n");

        Validator<String> notEmpty = s -> s != null && !s.isEmpty();
        Validator<String> notTooLong = s -> s != null && s.length() <= 10;
        Validator<String> noSpaces = s -> s != null && !s.contains(" ");

        // Chain validators using default methods
        Validator<String> usernameValidator = notEmpty.and(notTooLong).and(noSpaces);

        System.out.println("      Validating usernames:");
        testUsername(usernameValidator, "john123");
        testUsername(usernameValidator, "");
        testUsername(usernameValidator, "verylongusername");
        testUsername(usernameValidator, "has space");
    }

    private static void testUsername(Validator<String> v, String username) {
        String result = v.validate(username) ? "✅ Valid" : "❌ Invalid";
        String display = username.isEmpty() ? "(empty)" : username;
        System.out.printf("      \"%s\" → %s%n", display, result);
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 CUSTOM FUNCTIONAL INTERFACES\n");
        demoMathOperation();
        demoStringProcessor();
        demoValidator();
    }
}
