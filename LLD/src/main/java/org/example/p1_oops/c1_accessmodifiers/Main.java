package org.example.p1_oops.c1_accessmodifiers;

import org.example.p1_oops.c1_accessmodifiers.p1.Parent;
import org.example.p1_oops.c1_accessmodifiers.p1.SamePackageNeighbor;
import org.example.p1_oops.c1_accessmodifiers.p2.ChildInDiffPackage;
import org.example.p1_oops.c1_accessmodifiers.p2.StrangerInDiffPackage;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Access Modifier Demonstration ===\n");

        // 1. Same Class
        new Parent().printAccess();

        // 2. Same Package (Neighbor)
        new SamePackageNeighbor().checkAccess();

        // 3. Different Package (Child)
        new ChildInDiffPackage().checkAccess();

        // 4. Different Package (Stranger)
        new StrangerInDiffPackage().checkAccess();
    }
}
