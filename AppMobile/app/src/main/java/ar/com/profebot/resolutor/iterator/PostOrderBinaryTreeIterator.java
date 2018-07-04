package ar.com.profebot.resolutor.iterator;

import java.util.Stack;

import ar.com.profebot.parser.container.TreeNode;

public class PostOrderBinaryTreeIterator {
    Stack<TreeNode> stack = new Stack<TreeNode>();

    /** find the first leaf in a tree rooted at cur and store intermediate nodes */
    private void findNextLeaf(TreeNode cur) {
        while (cur != null) {
            stack.push(cur);
            if (cur.getLeftNode() != null) {
                cur = cur.getLeftNode();
            } else {
                cur = cur.getRightNode();
            }
        }
    }

    /** Constructor */
    public PostOrderBinaryTreeIterator(TreeNode root) {
        findNextLeaf(root);
    }

    /** Return true if traversal has not finished; otherwise, return false. */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /** Returns the next integer a in the post-order traversal of the given binary tree.
     * For example, given a binary tree below,
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     * the outputs will be 1, 3, 2, 5, 7, 6, 4.
     */
    public TreeNode next() {
        if (!hasNext()) {
            return null;
        }

        TreeNode res = stack.pop();
        if (!stack.isEmpty()) {
            TreeNode top = stack.peek();
            if (res == top.getLeftNode()) {
                findNextLeaf(top.getRightNode()); // find next leaf in right sub-tree
            }
        }

        return res;
    }
}
