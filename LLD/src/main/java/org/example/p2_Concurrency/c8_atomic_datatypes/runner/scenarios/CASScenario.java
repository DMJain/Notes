package org.example.p2_Concurrency.c8_atomic_datatypes.runner.scenarios;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates Compare-And-Set (CAS) operation.
 */
public class CASScenario {

    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           COMPARE-AND-SET (CAS) DEMO                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        AtomicInteger value = new AtomicInteger(5);

        System.out.println("  Initial value: " + value.get());
        System.out.println();

        // CAS success
        System.out.println("  Attempt: compareAndSet(5, 10)");
        boolean success1 = value.compareAndSet(5, 10);
        System.out.println("  Result: " + (success1 ? "SUCCESS ✅" : "FAILED ❌"));
        System.out.println("  Current value: " + value.get());
        System.out.println();

        // CAS failure (value is now 10, not 5)
        System.out.println("  Attempt: compareAndSet(5, 20)");
        boolean success2 = value.compareAndSet(5, 20);
        System.out.println("  Result: " + (success2 ? "SUCCESS ✅" : "FAILED ❌ (value was 10, not 5)"));
        System.out.println("  Current value: " + value.get());
        System.out.println();

        // CAS success with correct expected value
        System.out.println("  Attempt: compareAndSet(10, 20)");
        boolean success3 = value.compareAndSet(10, 20);
        System.out.println("  Result: " + (success3 ? "SUCCESS ✅" : "FAILED ❌"));
        System.out.println("  Current value: " + value.get());
        System.out.println();

        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │  CAS = Atomic \"check-then-update\"                       │");
        System.out.println("  │  • Succeeds only if current value equals expected       │");
        System.out.println("  │  • No lock needed - uses single CPU instruction         │");
        System.out.println("  │  • Failed operations just retry                         │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        System.out.println();
    }
}
