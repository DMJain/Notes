const Parent = require('../p1/Parent');

/**
 * Demonstrates access from a child class in a different module/package
 */
class ChildInDiffPackage extends Parent {
    checkAccess() {
        console.log("\n--- Inside ChildInDiffPackage (Different Package, Child Class) ---");

        console.log("publicVar: " + this.publicVar + " (Accessible)");
        console.log("protectedVar: " + this._protectedVar + " (Accessible via Inheritance - convention)");

        // In JS, defaultVar is still accessible since there's no true package-private
        console.log("defaultVar: " + this.defaultVar + " (Accessible - JS has no package-private)");

        // console.log("privateVar: " + this.#privateVar);
        // SyntaxError: Private field '#privateVar' must be declared in an enclosing class
        console.log("privateVar: NOT Accessible (Private fields with # are only visible within the declaring class)");
    }
}

module.exports = ChildInDiffPackage;
