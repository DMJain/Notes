package org.example.p1_oops.c7_composition.association;

// Association: Student HAS-A Teacher, but both exist INDEPENDENTLY
public class Student {
    private String name;
    private Teacher teacher; // Reference to Teacher (passed from outside)

    public Student(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher; // Teacher is NOT created here, just referenced
    }

    public void study() {
        System.out.println("Student " + name + " is studying " + teacher.getSubject() + " with " + teacher.getName());
    }

    // Teacher can exist without Student
    // Student can change Teacher
    // This is ASSOCIATION (weak relationship)
}
