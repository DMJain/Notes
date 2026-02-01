package org.example.p3_Java_Advance_Concept.c17_exception_best_practices.impl;

/**
 * Demonstrates exception handling best practices and anti-patterns.
 */
public class BestPracticesDemo {

    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║         EXCEPTION HANDLING BEST PRACTICES DEMO                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateAntiPatterns();
        demonstrateBestPractices();
        printGoldenRules();
    }

    private static void demonstrateAntiPatterns() {
        System.out.println("❌ ANTI-PATTERNS (What NOT to Do)");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  1. EMPTY CATCH BLOCK");
        System.out.println("     try { ... } catch (Exception e) { }  // TERRIBLE!");
        System.out.println("     → Debugging nightmare - failures are silent");
        System.out.println();

        System.out.println("  2. CATCHING TOO BROAD");
        System.out.println("     catch (Exception e) { ... }");
        System.out.println("     → Catches programming bugs (NPE, array bounds)");
        System.out.println();

        System.out.println("  3. EXCEPTIONS FOR FLOW CONTROL");
        System.out.println("     try { parse(x); } catch (...) { return false; }");
        System.out.println("     → Slow and unclear, use if/else instead");
        System.out.println();

        System.out.println("  4. LOG AND THROW (Don't do both!)");
        System.out.println("     logger.error(e); throw e;");
        System.out.println("     → Causes duplicate log entries");
        System.out.println();

        System.out.println("  5. RETURN IN FINALLY");
        System.out.println("     finally { return x; }");
        System.out.println("     → Hides exceptions completely!");
        System.out.println();
    }

    private static void demonstrateBestPractices() {
        System.out.println("✅ BEST PRACTICES (What TO Do)");
        System.out.println("─".repeat(60));
        System.out.println();

        System.out.println("  1. BE SPECIFIC");
        System.out.println("     catch (FileNotFoundException e) { ... }");
        System.out.println("     catch (IOException e) { ... }");
        System.out.println();

        System.out.println("  2. INCLUDE CONTEXT");
        System.out.println("     throw new OrderException(\"Failed for order: \" + id, cause);");
        System.out.println();

        System.out.println("  3. PRESERVE THE CAUSE");
        System.out.println("     throw new HighLevelEx(msg, originalException);");
        System.out.println();

        System.out.println("  4. USE TRY-WITH-RESOURCES");
        System.out.println("     try (Connection c = getConn()) { ... }");
        System.out.println();

        System.out.println("  5. FAIL FAST");
        System.out.println("     Objects.requireNonNull(arg, \"arg cannot be null\");");
        System.out.println();
    }

    private static void printGoldenRules() {
        System.out.println("═".repeat(60));
        System.out.println("                    GOLDEN RULES");
        System.out.println("═".repeat(60));
        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │ 1. CATCH what you can HANDLE meaningfully              │");
        System.out.println("  │ 2. PROPAGATE what you can't handle                     │");
        System.out.println("  │ 3. FAIL FAST - validate early                          │");
        System.out.println("  │ 4. PRESERVE CONTEXT - include cause and details        │");
        System.out.println("  │ 5. BE SPECIFIC - use appropriate exception types       │");
        System.out.println("  │ 6. NEVER SWALLOW - always log or handle                │");
        System.out.println("  │ 7. USE TRY-WITH-RESOURCES for cleanup                  │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        System.out.println();
    }
}
