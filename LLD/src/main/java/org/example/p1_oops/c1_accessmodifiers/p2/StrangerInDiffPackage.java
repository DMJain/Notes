package org.example.p1_oops.c1_accessmodifiers.p2;

import org.example.p1_oops.c1_accessmodifiers.p1.Parent;

public class StrangerInDiffPackage {
    public void checkAccess() {
        System.out.println("\n--- Inside StrangerInDiffPackage (Different Package, Non-Child Class) ---");
        Parent p = new Parent();

        System.out.println("publicVar: " + p.publicVar + " (Accessible)");

        // System.out.println("protectedVar: " + p.protectedVar);
        System.out.println("protectedVar: NOT Accessible (Protected is visible in diff package ONLY via inheritance)");

        // System.out.println("defaultVar: " + p.defaultVar);
        System.out.println("defaultVar: NOT Accessible (Default is only visible within the same package)");

        // System.out.println("privateVar: " + p.privateVar);
        System.out.println("privateVar: NOT Accessible (Private is only visible within the same class)");
    }
}
