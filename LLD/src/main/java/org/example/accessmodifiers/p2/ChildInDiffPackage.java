package org.example.accessmodifiers.p2;

import org.example.accessmodifiers.p1.Parent;

public class ChildInDiffPackage extends Parent {
    public void checkAccess() {
        System.out.println("\n--- Inside ChildInDiffPackage (Different Package, Child Class) ---");

        System.out.println("publicVar: " + publicVar + " (Accessible)");
        System.out.println("protectedVar: " + protectedVar + " (Accessible via Inheritance)");

        // System.out.println("defaultVar: " + defaultVar);
        System.out.println("defaultVar: NOT Accessible (Default is only visible within the same package)");

        // System.out.println("privateVar: " + privateVar);
        System.out.println("privateVar: NOT Accessible (Private is only visible within the same class)");
    }
}
