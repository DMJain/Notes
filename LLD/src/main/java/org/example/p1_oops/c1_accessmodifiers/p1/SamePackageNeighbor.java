package org.example.p1_oops.c1_accessmodifiers.p1;

public class SamePackageNeighbor {
    public void checkAccess() {
        System.out.println("\n--- Inside SamePackageNeighbor (Same Package, Different Class) ---");
        Parent p = new Parent();

        System.out.println("publicVar: " + p.publicVar + " (Accessible)");
        System.out.println("protectedVar: " + p.protectedVar + " (Accessible)");
        System.out.println("defaultVar: " + p.defaultVar + " (Accessible)");

        // System.out.println("privateVar: " + p.privateVar);
        System.out.println("privateVar: NOT Accessible (Private is only visible within the same class)");
    }
}
