/**
 * Teacher class for demonstrating Association
 * Teacher exists independently and can be shared by multiple Students
 */
class Teacher {
    #name;
    #subject;

    constructor(name, subject) {
        this.#name = name;
        this.#subject = subject;
    }

    getName() {
        return this.#name;
    }

    getSubject() {
        return this.#subject;
    }

    teach() {
        console.log("Teacher " + this.#name + " is teaching " + this.#subject);
    }
}

module.exports = Teacher;
