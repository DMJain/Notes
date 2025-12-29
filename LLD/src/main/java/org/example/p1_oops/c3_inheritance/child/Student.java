package org.example.p1_oops.c3_inheritance.child;

import org.example.p1_oops.c3_inheritance.base.User;

public class Student extends User {
    public String batchName;

    public Student(String name, String email, String batchName) {
        super(name, email); // Calls User constructor
        this.batchName = batchName;
        System.out.println("  [Student Constructor] Called for: " + this.name);
    }

    @Override
    public void solveProblem() {
        System.out.println(name + " (Student) is solving a DSA problem for batch: " + batchName);
    }
}
