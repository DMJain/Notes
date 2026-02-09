package org.example.Q1382_BalanceABinarySearchTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 1382. Balance a Binary Search Tree
 * 
 * Approach: In-order Traversal + Divide & Conquer
 * - In-order traverse BST → nodes in sorted order
 * - Build balanced BST: pick middle as root recursively
 * 
 * Time: O(n) - each node visited twice
 * Space: O(n) - store all nodes in list
 */
public class BalanceABinarySearchTree {

    // Definition for a binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    List<TreeNode> a = new ArrayList<>();

    public TreeNode balanceBST(TreeNode root) {
        inorder(root);
        return makeBST(0, a.size() - 1);
    }

    // In-order traversal: Left → Node → Right (gives sorted order for BST)
    private void inorder(TreeNode root) {
        if (root == null)
            return;
        inorder(root.left);
        a.add(root);
        inorder(root.right);
    }

    // Build balanced BST from sorted list: pick middle as root
    private TreeNode makeBST(int l, int r) {
        if (l > r)
            return null;
        int mid = (l + r) / 2;
        a.get(mid).left = makeBST(l, mid - 1);
        a.get(mid).right = makeBST(mid + 1, r);
        return a.get(mid);
    }

    // Convert tree to array representation (level-order with nulls)
    private static String toArray(TreeNode root) {
        if (root == null)
            return "[]";
        List<String> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                result.add("null");
            } else {
                result.add(String.valueOf(node.val));
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        // Trim trailing nulls
        while (!result.isEmpty() && result.get(result.size() - 1).equals("null")) {
            result.remove(result.size() - 1);
        }
        return "[" + String.join(",", result) + "]";
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        // Example 1: Right-skewed tree
        BalanceABinarySearchTree sol1 = new BalanceABinarySearchTree();
        TreeNode ex1 = new TreeNode(1);
        ex1.right = new TreeNode(2);
        ex1.right.right = new TreeNode(3);
        ex1.right.right.right = new TreeNode(4);

        System.out.println("Example 1:");
        System.out.println("  Input:  " + toArray(ex1));
        TreeNode balanced1 = sol1.balanceBST(ex1);
        System.out.println("  Output: " + toArray(balanced1));
        System.out.println();

        // Example 2: Already balanced
        BalanceABinarySearchTree sol2 = new BalanceABinarySearchTree();
        TreeNode ex2 = new TreeNode(2, new TreeNode(1), new TreeNode(3));

        System.out.println("Example 2:");
        System.out.println("  Input:  " + toArray(ex2));
        TreeNode balanced2 = sol2.balanceBST(ex2);
        System.out.println("  Output: " + toArray(balanced2));
    }
}
