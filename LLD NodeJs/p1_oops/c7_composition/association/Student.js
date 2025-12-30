/**
 * Student class demonstrating Association (weak HAS-A relationship)
 * 
 * Association: Student HAS-A Teacher, but both exist INDEPENDENTLY.
 * The Teacher is passed from outside, not created inside.
 */
class Student {
    #name;
    #teacher; // Reference to Teacher (passed from outside)

    constructor(name, teacher) {
        this.#name = name;
        this.#teacher = teacher; // Teacher is NOT created here, just referenced
    }

    study() {
        console.log("Student " + this.#name + " is studying " +
            this.#teacher.getSubject() + " with " + this.#teacher.getName());
    }

    // Teacher can exist without Student
    // Student can change Teacher
    // This is ASSOCIATION (weak relationship)
}

module.exports = Student;
