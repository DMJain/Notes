package org.example.p1_oops.c7_composition.association;

public class Teacher {
    private String name;
    private String subject;

    public Teacher(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public void teach() {
        System.out.println("Teacher " + name + " is teaching " + subject);
    }
}
