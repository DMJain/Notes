package org.example.p3_Java_Advance_Concept.c11_exception_basics.model.enums;

/**
 * Categorizes exceptions into their fundamental types.
 * <p>
 * Understanding this categorization is crucial for proper exception handling.
 * </p>
 */
public enum ExceptionCategory {

    /**
     * Errors are serious problems that applications should NOT try to catch.
     * <p>
     * Examples: OutOfMemoryError, StackOverflowError
     * </p>
     * <p>
     * These indicate JVM-level problems that cannot be recovered from.
     * </p>
     */
    ERROR("JVM-level problem - DO NOT CATCH", false, false),

    /**
     * Checked exceptions MUST be handled or declared.
     * <p>
     * The compiler forces you to either:
     * <ul>
     * <li>Catch them with try-catch</li>
     * <li>Declare them with throws clause</li>
     * </ul>
     * </p>
     * <p>
     * Examples: IOException, SQLException, ClassNotFoundException
     * </p>
     */
    CHECKED_EXCEPTION("Compiler-enforced handling required", true, true),

    /**
     * Unchecked exceptions (RuntimeExceptions) are optional to handle.
     * <p>
     * The compiler does NOT force you to handle these.
     * They typically indicate programming bugs that should be fixed.
     * </p>
     * <p>
     * Examples: NullPointerException, ArrayIndexOutOfBoundsException
     * </p>
     */
    UNCHECKED_EXCEPTION("Optional handling - usually indicates a bug", true, false);

    private final String description;
    private final boolean isRecoverable;
    private final boolean requiresHandling;

    ExceptionCategory(String description, boolean isRecoverable, boolean requiresHandling) {
        this.description = description;
        this.isRecoverable = isRecoverable;
        this.requiresHandling = requiresHandling;
    }

    /**
     * Gets a human-readable description of this category.
     *
     * @return Description of the category
     */
    public String getDescription() {
        return description;
    }

    /**
     * Indicates if exceptions in this category are typically recoverable.
     *
     * @return true if the application can potentially recover
     */
    public boolean isRecoverable() {
        return isRecoverable;
    }

    /**
     * Indicates if the compiler requires handling/declaring this category.
     *
     * @return true if handling is compiler-enforced
     */
    public boolean requiresHandling() {
        return requiresHandling;
    }

    /**
     * Prints a formatted summary of this category.
     */
    public void printSummary() {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.printf("│ Category: %-37s │%n", this.name());
        System.out.println("├─────────────────────────────────────────────────┤");
        System.out.printf("│ Description: %-34s │%n", description);
        System.out.printf("│ Recoverable: %-34s │%n", isRecoverable ? "Yes" : "No");
        System.out.printf("│ Requires Handling: %-28s │%n",
                requiresHandling ? "Yes (compiler enforced)" : "No (optional)");
        System.out.println("└─────────────────────────────────────────────────┘");
    }
}
