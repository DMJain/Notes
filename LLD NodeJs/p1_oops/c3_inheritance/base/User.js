/**
 * Base User class demonstrating inheritance in JavaScript
 */
class User {
    // Protected by convention (using _) - accessible to subclasses
    _name;
    _email;

    constructor(name, email) {
        this._name = name;
        this._email = email;
        console.log("  [User Constructor] Called for: " + this._name);
    }

    login() {
        console.log(this._name + " has logged in.");
    }

    solveProblem() {
        console.log(this._name + " (User) is solving a generic problem.");
    }

    // Getters for protected fields
    get name() {
        return this._name;
    }

    get email() {
        return this._email;
    }
}

module.exports = User;
