/**
 * Parent class demonstrating access modifiers in JavaScript
 * 
 * Note: JavaScript doesn't have traditional access modifiers like Java.
 * - # prefix = private (ES2022)
 * - No prefix = public (default)
 * - _ prefix = convention for "protected" (not enforced)
 * - Modules can simulate package-level access
 */
class Parent {
    // Public (accessible from anywhere)
    publicVar = "Public";
    
    // Protected (by convention using _ prefix - not enforced)
    _protectedVar = "Protected";
    
    // Default/Package (simulated via module exports - we export this class)
    defaultVar = "Default";
    
    // Private (ES2022 private fields using #)
    #privateVar = "Private";

    printAccess() {
        console.log("\n--- Inside Parent (Same Class) ---");
        console.log("publicVar: " + this.publicVar + " (Accessible)");
        console.log("protectedVar: " + this._protectedVar + " (Accessible)");
        console.log("defaultVar: " + this.defaultVar + " (Accessible)");
        console.log("privateVar: " + this.#privateVar + " (Accessible)");
    }

    // Getter for private var (for testing purposes)
    getPrivateVar() {
        return this.#privateVar;
    }
}

module.exports = Parent;
