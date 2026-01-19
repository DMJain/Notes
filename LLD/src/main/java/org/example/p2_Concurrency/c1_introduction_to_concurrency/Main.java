package org.example.p2_Concurrency.c1_introduction_to_concurrency;

/**
 * Introduction to Concurrency - Entry Point
 * 
 * This chapter covers the theoretical foundations of concurrency:
 * - What are processes?
 * - Why do we need concurrency?
 * - Concurrent vs Parallel execution
 * - Real-world examples (Google Docs, Spotify, Lightroom)
 * 
 * For hands-on code examples with threads, see c2_threads_in_java.
 * 
 * @see ../c2_threads_in_java/Main.java for practical thread examples
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║       CHAPTER 1: INTRODUCTION TO CONCURRENCY                  ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                               ║");
        System.out.println("║  This chapter is THEORETICAL. Key concepts covered:           ║");
        System.out.println("║                                                               ║");
        System.out.println("║  ✓ What is a Process?                                         ║");
        System.out.println("║  ✓ Why Concurrency Exists (The Frozen Screen Problem)         ║");
        System.out.println("║  ✓ CPU Scheduling & Context Switching                         ║");
        System.out.println("║  ✓ Concurrent vs Parallel Execution                           ║");
        System.out.println("║  ✓ Real-World Examples (Google Docs, Spotify, Lightroom)      ║");
        System.out.println("║                                                               ║");
        System.out.println("║  📖 Read: ConcurrencyNotes.md for detailed explanations       ║");
        System.out.println("║                                                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║  → Next: c2_threads_in_java for HANDS-ON coding!              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        demonstrateConcurrencyVsSequential();
    }

    /**
     * Simple demonstration showing how Java can report system thread/core info.
     */
    private static void demonstrateConcurrencyVsSequential() {
        System.out.println("\n📊 System Information:");
        System.out.println("─────────────────────────────────────────────");

        // Get available processors (cores)
        int availableCores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available CPU cores: " + availableCores);

        // Get current thread info
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread name: " + currentThread.getName());
        System.out.println("Current thread ID:   " + currentThread.threadId());

        // Demonstrate that main runs on a single thread by default
        System.out.println("\n💡 Key Insight:");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("Your system has " + availableCores + " cores, but this program");
        System.out.println("runs on just 1 thread ('" + currentThread.getName() + "').");
        System.out.println("In c2, we'll learn to use ALL " + availableCores + " cores!");
    }
}
