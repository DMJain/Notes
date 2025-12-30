const Parent = require('./p1/Parent');
const SamePackageNeighbor = require('./p1/SamePackageNeighbor');
const ChildInDiffPackage = require('./p2/ChildInDiffPackage');
const StrangerInDiffPackage = require('./p2/StrangerInDiffPackage');

function main() {
    console.log("=== Access Modifier Demonstration (JavaScript) ===\n");

    // 1. Same Class
    new Parent().printAccess();

    // 2. Same Package (Neighbor)
    new SamePackageNeighbor().checkAccess();

    // 3. Different Package (Child)
    new ChildInDiffPackage().checkAccess();

    // 4. Different Package (Stranger)
    new StrangerInDiffPackage().checkAccess();
}

main();
