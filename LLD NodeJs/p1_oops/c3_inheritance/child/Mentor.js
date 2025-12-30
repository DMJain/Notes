const User = require('../base/User');

/**
 * Mentor class extending User - demonstrates inheritance
 */
class Mentor extends User {
    company;

    constructor(name, email, company) {
        super(name, email);
        this.company = company;
        console.log("  [Mentor Constructor] Called for: " + this._name);
    }

    // Method override
    solveProblem() {
        console.log(this._name + " (Mentor from " + this.company + ") is solving a System Design problem.");
    }
}

module.exports = Mentor;
