const Parent = require('../p1/Parent');

/**
 * Demonstrates access from an unrelated class in a different module/package
 */
class StrangerInDiffPackage {
    checkAccess() {
        console.log("\n--- Inside StrangerInDiffPackage (Different Package, Non-Child Class) ---");
        const p = new Parent();

        console.log("publicVar: " + p.publicVar + " (Accessible)");

        // In JS, there's no enforcement of protected - it's just convention
        console.log("protectedVar: " + p._protectedVar + " (Accessible - JS convention, NOT enforced)");

        // In JS, there's no package-private concept
        console.log("defaultVar: " + p.defaultVar + " (Accessible - JS has no package-private)");

        // console.log("privateVar: " + p.#privateVar);
        // SyntaxError: Private field '#privateVar' must be declared in an enclosing class
        console.log("privateVar: NOT Accessible (Private fields with # are only visible within the class)");
    }
}

module.exports = StrangerInDiffPackage;
