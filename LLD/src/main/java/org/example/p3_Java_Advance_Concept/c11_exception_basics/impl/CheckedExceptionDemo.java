package org.example.p3_Java_Advance_Concept.c11_exception_basics.impl;

import org.example.p3_Java_Advance_Concept.c11_exception_basics.contracts.IExceptionDemo;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Demonstrates CHECKED exceptions - those the compiler FORCES you to handle.
 * <p>
 * Key Learning Points:
 * <ul>
 * <li>Checked exceptions extend Exception (but NOT RuntimeException)</li>
 * <li>Compiler FORCES you to either catch them OR declare with throws</li>
 * <li>Typically represent external, recoverable failures (file, network,
 * DB)</li>
 * </ul>
 * </p>
 */
public class CheckedExceptionDemo implements IExceptionDemo {

    @Override
    public String getDemoName() {
        return "Checked Exception Demonstration";
    }

    @Override
    public String getDescription() {
        return "Shows how checked exceptions MUST be handled or declared - the compiler enforces it!";
    }

    @Override
    public void demonstrate() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              CHECKED EXCEPTION DEMONSTRATION                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printExplanation();
        demonstrateFileNotFoundException();
        demonstrateThrowsClause();
    }

    private void printExplanation() {
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ WHAT ARE CHECKED EXCEPTIONS?                                     │");
        System.out.println("├──────────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                                  │");
        System.out.println("│ • Exceptions that the COMPILER forces you to handle             │");
        System.out.println("│ • You MUST either:                                               │");
        System.out.println("│   1. Catch them with try-catch, OR                              │");
        System.out.println("│   2. Declare them with 'throws' clause                          │");
        System.out.println("│                                                                  │");
        System.out.println("│ • They extend Exception (but NOT RuntimeException)              │");
        System.out.println("│ • They represent EXTERNAL failures that we CAN'T prevent        │");
        System.out.println("│   but CAN recover from (retry, use backup, inform user)         │");
        System.out.println("│                                                                  │");
        System.out.println("│ COMMON EXAMPLES:                                                 │");
        System.out.println("│   • IOException - File/network I/O failed                       │");
        System.out.println("│   • SQLException - Database error                               │");
        System.out.println("│   • ClassNotFoundException - Class not found                    │");
        System.out.println("│   • InterruptedException - Thread was interrupted               │");
        System.out.println("│                                                                  │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    /**
     * Demonstrates FileNotFoundException - a classic checked exception.
     * <p>
     * Notice how we MUST handle it with try-catch!
     * If we removed the try-catch, the code would NOT COMPILE.
     * </p>
     */
    private void demonstrateFileNotFoundException() {
        System.out.println("📁 Demo 1: FileNotFoundException (Checked Exception)");
        System.out.println("─".repeat(60));

        String filename = "non_existent_file_that_does_not_exist.txt";
        System.out.println("Attempting to open file: " + filename);
        System.out.println();

        // WITHOUT try-catch, this would NOT compile!
        // The compiler FORCES us to handle FileNotFoundException
        try {
            FileReader reader = new FileReader(filename);
            System.out.println("File opened successfully!"); // Won't reach here
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("✗ CAUGHT: FileNotFoundException!");
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  ↳ This is EXPECTED - the file doesn't exist!");
            System.out.println();
            System.out.println("What we can do now (Graceful Recovery):");
            System.out.println("  ✓ Show user-friendly message");
            System.out.println("  ✓ Create the file if needed");
            System.out.println("  ✓ Use a default/backup file");
        } catch (Exception e) {
            System.out.println("Some other exception: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Demonstrates using the 'throws' clause to propagate exceptions.
     */
    private void demonstrateThrowsClause() {
        System.out.println("📤 Demo 2: Using 'throws' clause to propagate exceptions");
        System.out.println("─".repeat(60));
        System.out.println();
        System.out.println("Instead of catching, we can DECLARE with 'throws':");
        System.out.println();
        System.out.println("    // This method says: \"I might throw this exception!\"");
        System.out.println("    // The CALLER is responsible for handling it.");
        System.out.println("    public void readConfig() throws IOException {");
        System.out.println("        FileReader reader = new FileReader(\"config.txt\");");
        System.out.println("        // ... read file ...");
        System.out.println("    }");
        System.out.println();
        System.out.println("Why use 'throws'?");
        System.out.println("  ✓ When you CAN'T handle the exception at this level");
        System.out.println("  ✓ When the CALLER is better equipped to decide what to do");
        System.out.println("  ✓ When you want to let the exception propagate up");
        System.out.println();

        // Let's actually call a method that throws
        try {
            String content = readFile("missing.txt");
            System.out.println("Content: " + content);
        } catch (FileNotFoundException e) {
            System.out.println("✓ Exception propagated and caught here!");
            System.out.println("  The readFile() method declared 'throws FileNotFoundException'");
            System.out.println("  So WE had to handle it here.");
        }
        System.out.println();
    }

    /**
     * A method that DECLARES it throws FileNotFoundException.
     * <p>
     * Notice the 'throws' clause - this tells the compiler AND the caller
     * that this method might throw this exception.
     * </p>
     *
     * @param filename The file to read
     * @return The file contents (if successful)
     * @throws FileNotFoundException if the file doesn't exist
     */
    private String readFile(String filename) throws FileNotFoundException {
        // If this throws, it propagates to the caller
        FileReader reader = new FileReader(filename);
        return "File contents would be here";
    }
}
