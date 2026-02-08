package org.example.Q0110_BalancedBinaryTree;

/**
 * 110. Balanced Binary Tree
 * 
 * Approach: Bottom-Up DFS with Sentinel Value
 * - Use -1 as sentinel to indicate "already unbalanced" (short-circuit)
 * - Post-order traversal: process children before parent
 * - Return actual height if balanced, -1 if any subtree is unbalanced
 * 
 * Time: O(n) - visit each node exactly once
 * Space: O(n) - recursion stack for skewed tree
 */
public class BalancedBinaryTree {

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

    /**
     * Check if binary tree is height-balanced.
     * Uses bottom-up DFS with -1 sentinel for early termination.
     */
    public boolean isBalanced(TreeNode root) {
        return dfs(root) != -1;
    }

    /**
     * Returns height of subtree if balanced, -1 if unbalanced.
     * 
     * Key insight: -1 propagates up immediately when imbalance detected,
     * avoiding unnecessary computation in remaining subtrees.
     */
    private int dfs(TreeNode node) {
        // Base case: null node has height 0
        if (node == null) {
            return 0;
        }

        // Get left subtree height (or -1 if unbalanced)
        int left = dfs(node.left);
        if (left == -1)
            return -1; // Short-circuit: left already unbalanced

        // Get right subtree height (or -1 if unbalanced)
        int right = dfs(node.right);
        if (right == -1)
            return -1; // Short-circuit: right already unbalanced

        // Check balance at current node
        if (Math.abs(left - right) > 1) {
            return -1; // Current node is unbalanced
        }

        // Return height: 1 (current node) + max subtree height
        return 1 + Math.max(left, right);
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        BalancedBinaryTree solution = new BalancedBinaryTree();

        // Example 1: [3,9,20,null,null,15,7] -> true
        // 3
        // / \
        // 9 20
        // / \
        // 15 7
        TreeNode ex1 = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20,
                        new TreeNode(15),
                        new TreeNode(7)));
        System.out.println("Example 1: " + solution.isBalanced(ex1) + " (expected: true)");

        // Example 2: [1,2,2,3,3,null,null,4,4] -> false
        // 1
        // / \
        // 2 2
        // / \
        // 3 3
        // / \
        // 4 4
        TreeNode ex2 = new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(3,
                                new TreeNode(4),
                                new TreeNode(4)),
                        new TreeNode(3)),
                new TreeNode(2));
        System.out.println("Example 2: " + solution.isBalanced(ex2) + " (expected: false)");

        // Example 3: [] -> true
        TreeNode ex3 = null;
        System.out.println("Example 3: " + solution.isBalanced(ex3) + " (expected: true)");

        // Edge case: Single node -> true
        TreeNode single = new TreeNode(1);
        System.out.println("Single node: " + solution.isBalanced(single) + " (expected: true)");

        // Edge case: Perfectly unbalanced (height diff = 2)
        // 1
        // /
        // 2
        // /
        // 3
        TreeNode skewed = new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(3),
                        null),
                null);
        System.out.println("Skewed tree: " + solution.isBalanced(skewed) + " (expected: false)");
    }
}
