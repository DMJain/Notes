const Parent = require('./Parent');

/**
 * Demonstrates access within the same module/package
 * In JS, "same package" = same directory/module
 */
class SamePackageNeighbor {
    checkAccess() {
        console.log("\n--- Inside SamePackageNeighbor (Same Package, Different Class) ---");
        const p = new Parent();

        console.log("publicVar: " + p.publicVar + " (Accessible)");
        console.log("protectedVar: " + p._protectedVar + " (Accessible - convention only)");
        console.log("defaultVar: " + p.defaultVar + " (Accessible)");

        // console.log("privateVar: " + p.#privateVar);
        // SyntaxError: Private field '#privateVar' must be declared in an enclosing class
        console.log("privateVar: NOT Accessible (Private fields with # are only visible within the class)");
    }
}

module.exports = SamePackageNeighbor;
