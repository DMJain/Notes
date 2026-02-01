package org.example.p3_Java_Advance_Concept.c11_exception_basics.runner.scenarios;

/**
 * Demonstrates the Java Exception Hierarchy visually.
 * <p>
 * This scenario prints a detailed ASCII representation of the
 * exception class hierarchy to help understand the relationships.
 * </p>
 */
public class ExceptionHierarchyScenario {

    /**
     * Executes the hierarchy visualization scenario.
     */
    public void execute() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              JAVA EXCEPTION HIERARCHY                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        printHierarchyTree();
        explainEachLevel();
        showInheritanceDemo();
    }

    private void printHierarchyTree() {
        System.out.println("                         java.lang.Object");
        System.out.println("                               │");
        System.out.println("                               ▼");
        System.out.println("                    ┌───────────────────┐");
        System.out.println("                    │   Throwable       │  ← Root of all exceptions");
        System.out.println("                    │   (can be thrown) │");
        System.out.println("                    └─────────┬─────────┘");
        System.out.println("                              │");
        System.out.println("              ┌───────────────┴───────────────┐");
        System.out.println("              │                               │");
        System.out.println("              ▼                               ▼");
        System.out.println("    ┌─────────────────┐            ┌─────────────────┐");
        System.out.println("    │     Error       │            │   Exception     │");
        System.out.println("    │                 │            │                 │");
        System.out.println("    │ JVM is DYING    │            │ Recoverable     │");
        System.out.println("    │ ⚠️ DON'T CATCH! │            │ situations      │");
        System.out.println("    └────────┬────────┘            └────────┬────────┘");
        System.out.println("             │                              │");
        System.out.println("    ┌────────┼────────┐           ┌─────────┴─────────┐");
        System.out.println("    ▼        ▼        ▼           ▼                   ▼");
        System.out.println("  OutOf   Stack    Virtual    ┌──────────┐    ┌─────────────┐");
        System.out.println("  Memory  Overflow Machine    │ CHECKED  │    │RuntimeExcep │");
        System.out.println("  Error   Error    Error      │Exceptions│    │   tion      │");
        System.out.println("                              │          │    │ (UNCHECKED) │");
        System.out.println("                              └────┬─────┘    └──────┬──────┘");
        System.out.println("                                   │                 │");
        System.out.println("                          ┌────────┼────┐    ┌───────┼───────┐");
        System.out.println("                          ▼        ▼    ▼    ▼       ▼       ▼");
        System.out.println("                       IOExc   SQLExc  Class  NPE   AIOOB  Arith");
        System.out.println("                       eption  eption  NotFnd Excep Except metic");
        System.out.println("                                       Excep  tion  ion    Excep");
        System.out.println();
    }

    private void explainEachLevel() {
        System.out.println("═".repeat(70));
        System.out.println("                    WHAT EACH LEVEL MEANS");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ LEVEL 1: Throwable                                              │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ • The root class that can be thrown with 'throw' keyword       │");
        System.out.println("│ • Contains the message and stack trace                         │");
        System.out.println("│ • You rarely use this directly                                 │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ LEVEL 2a: Error (extends Throwable)                             │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ • Serious problems with the JVM itself                         │");
        System.out.println("│ • OutOfMemoryError: JVM ran out of memory                      │");
        System.out.println("│ • StackOverflowError: Too many method calls (infinite loop?)   │");
        System.out.println("│                                                                 │");
        System.out.println("│ ⚠️  DO NOT CATCH THESE! You cannot recover from them.          │");
        System.out.println("│     If the JVM is dying, let it die gracefully.                │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ LEVEL 2b: Exception (extends Throwable)                         │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ • Problems that your APPLICATION can potentially handle        │");
        System.out.println("│ • Split into two categories: Checked and Unchecked             │");
        System.out.println("│                                                                 │");
        System.out.println("│ ✅ These are the ones you work with in everyday coding!        │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ LEVEL 3a: Checked Exceptions (extend Exception)                 │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ • IOException, SQLException, ClassNotFoundException...         │");
        System.out.println("│ • Compiler FORCES you to handle them                           │");
        System.out.println("│ • Represent external failures (files, network, DB)             │");
        System.out.println("│ • You MUST either: try-catch OR declare with throws            │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ LEVEL 3b: RuntimeException (Unchecked)                          │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ • NullPointerException, ArrayIndexOutOfBoundsException...      │");
        System.out.println("│ • Compiler does NOT force you to handle them                   │");
        System.out.println("│ • Represent PROGRAMMING BUGS                                   │");
        System.out.println("│ • Best practice: FIX THE BUG, don't catch it!                  │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private void showInheritanceDemo() {
        System.out.println("═".repeat(70));
        System.out.println("                    INHERITANCE IN ACTION");
        System.out.println("═".repeat(70));
        System.out.println();

        System.out.println("Let's verify the hierarchy with actual Java code:");
        System.out.println();

        // Demonstrate inheritance
        Exception exception = new Exception("test");
        RuntimeException runtimeException = new RuntimeException("test");
        NullPointerException npe = new NullPointerException("test");

        System.out.println("  NullPointerException instanceof RuntimeException: " +
                (npe instanceof RuntimeException));
        System.out.println("  NullPointerException instanceof Exception:        " +
                (npe instanceof Exception));
        System.out.println("  NullPointerException instanceof Throwable:        " +
                (npe instanceof Throwable));
        System.out.println();

        System.out.println("  RuntimeException instanceof Exception: " +
                (runtimeException instanceof Exception));
        System.out.println("  Exception instanceof Throwable:        " +
                (exception instanceof Throwable));
        System.out.println();

        System.out.println("  This is why catch(Exception e) catches RuntimeExceptions too!");
        System.out.println("  RuntimeException IS-A Exception (inheritance).");
        System.out.println();
    }
}
