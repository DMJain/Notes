package org.example.inheritance.base;

public class User {
    protected String name;
    protected String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        System.out.println("  [User Constructor] Called for: " + this.name);
    }

    public void login() {
        System.out.println(name + " has logged in.");
    }

    public void solveProblem() {
        System.out.println(name + " (User) is solving a generic problem.");
    }
}
