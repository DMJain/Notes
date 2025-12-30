/**
 * Engine class for demonstrating Composition
 * Engine is owned by Car and cannot exist independently
 */
class Engine {
    #type;

    constructor(type) {
        this.#type = type;
        console.log("  Engine created: " + type);
    }

    start() {
        console.log("  Engine (" + this.#type + ") started.");
    }

    stop() {
        console.log("  Engine (" + this.#type + ") stopped.");
    }
}

module.exports = Engine;
