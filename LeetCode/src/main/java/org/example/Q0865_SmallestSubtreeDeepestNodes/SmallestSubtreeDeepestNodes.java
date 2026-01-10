package org.example.Q0865_SmallestSubtreeDeepestNodes;

// DFS: Return pair (depth, node). If left/right depths equal, current is LCA. Else pick deeper side.
public class SmallestSubtreeDeepestNodes {

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

    private static class Pair {
        int depth;
        TreeNode node;

        Pair(int d, TreeNode node) {
            this.depth = d;
            this.node = node;
        }
    }

    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        Pair ans = dfs(root);
        return ans.node;
    }

    private Pair dfs(TreeNode root) {
        if (root == null)
            return new Pair(0, null);

        Pair l = dfs(root.left);
        Pair r = dfs(root.right);

        // If left is deeper, the answer must be in the left subtree
        if (l.depth > r.depth) {
            return new Pair(l.depth + 1, l.node);
        }

        // If right is deeper, the answer must be in the right subtree
        if (r.depth > l.depth) {
            return new Pair(r.depth + 1, r.node);
        }

        // If depths are equal, THIS node is the LCA of the deepest nodes
        return new Pair(l.depth + 1, root);
    }

    public static void main(String[] args) {
        SmallestSubtreeDeepestNodes solution = new SmallestSubtreeDeepestNodes();

        // Example 1: [3,5,1,6,2,0,8,null,null,7,4]
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);

        TreeNode result = solution.subtreeWithAllDeepest(root);
        System.out.println("Result Node Val: " + result.val); // Expected: 2

        // Example 2: [1]
        TreeNode root2 = new TreeNode(1);
        System.out.println("Result Node Val: " + solution.subtreeWithAllDeepest(root2).val); // Expected: 1
    }
}
