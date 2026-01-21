package org.example.p2_Concurrency.c5_synchronization_problem;

import org.example.p2_Concurrency.c5_synchronization_problem.runner.ProblemRunner;

/**
 * Entry point for the Synchronization Problem module.
 * 
 * <p>
 * This module demonstrates WHY synchronization is necessary
 * by showing the problems that occur without it.
 * </p>
 * 
 * <h2>What This Module Covers</h2>
 * <ul>
 * <li>Race conditions with the Adder-Subtractor problem</li>
 * <li>Why count++ is not atomic</li>
 * <li>Critical sections and preemption</li>
 * <li>Properties of a good synchronization solution</li>
 * </ul>
 * 
 * @see org.example.p2_Concurrency.c6_mutex_locks for the solution
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Synchronization Problem Module ===");
        ProblemRunner.runAllScenarios();
    }
}
