/**
 * Student class demonstrating constructors, deep copy, and shallow copy in JavaScript
 */
class Student {
    name;
    age;
    examScores; // Array to demonstrate Deep vs Shallow copy

    // 1. Constructor (handles both default and parameterized)
    constructor(nameOrStudent = "Unknown", age = 18, scores = [0, 0, 0]) {
        // Check if first argument is a Student instance (Copy Constructor pattern)
        if (nameOrStudent instanceof Student) {
            const other = nameOrStudent;
            this.name = other.name;
            this.age = other.age;
            // Deep Copy: Create a NEW array and copy values
            this.examScores = [...other.examScores];
        } else {
            // Regular constructor
            this.name = nameOrStudent;
            this.age = age;
            this.examScores = scores;
        }
    }

    // Method to demonstrate Shallow Copy assignment
    shallowCopyFrom(other) {
        this.name = other.name;
        this.age = other.age;
        this.examScores = other.examScores; // Copying REFERENCE, not creating new array
    }

    toString() {
        return `Student{name='${this.name}', age=${this.age}, scores=[${this.examScores.join(', ')}]}`;
    }
}

module.exports = Student;
