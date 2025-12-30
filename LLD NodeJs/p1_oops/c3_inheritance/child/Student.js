const User = require('../base/User');

/**
 * Student class extending User - demonstrates inheritance
 */
class Student extends User {
    batchName;

    constructor(name, email, batchName) {
        super(name, email); // Calls User constructor
        this.batchName = batchName;
        console.log("  [Student Constructor] Called for: " + this._name);
    }

    // Method override
    solveProblem() {
        console.log(this._name + " (Student) is solving a DSA problem for batch: " + this.batchName);
    }
}

module.exports = Student;
