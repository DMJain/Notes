package org.example.p1_oops.c1_accessmodifiers.p1;

public class Parent {
    public String publicVar = "Public";
    protected String protectedVar = "Protected";
    String defaultVar = "Default";
    private String privateVar = "Private";

    public void printAccess() {
        System.out.println("\n--- Inside Parent (Same Class) ---");
        System.out.println("publicVar: " + publicVar + " (Accessible)");
        System.out.println("protectedVar: " + protectedVar + " (Accessible)");
        System.out.println("defaultVar: " + defaultVar + " (Accessible)");
        System.out.println("privateVar: " + privateVar + " (Accessible)");
    }
}
