package org.example.inheritance.child;

import org.example.inheritance.base.User;

public class Mentor extends User {
    public String company;

    public Mentor(String name, String email, String company) {
        super(name, email);
        this.company = company;
        System.out.println("  [Mentor Constructor] Called for: " + this.name);
    }

    @Override
    public void solveProblem() {
        System.out.println(name + " (Mentor from " + company + ") is solving a System Design problem.");
    }
}
