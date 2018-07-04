package ar.com.profebot.resolutor.iterator;

import java.util.Stack;

import ar.com.profebot.parser.container.TreeNode;

public class InOrderBinaryTreeIterator {
    /**
     * Find the left-most node of the root and store previous left children in a stack;
     * Pop up the top node from the stack;
     * If it has a right child, find the lef-most node of the right child and store left children in the stack.
     */
    Stack<TreeNode> stack = new Stack<TreeNode>();

    /** Push node cur and all of its left children into stack */
    private void pushLeftChildren(TreeNode cur) {
        while (cur != null) {
            stack.push(cur);
            cur = cur.getLeftNode();
        }
    }

    /** Constructor */
    public InOrderBinaryTreeIterator(TreeNode root) {
        pushLeftChildren(root);
    }

    /** Return true if traversal has not finished; otherwise, return false.
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /** Returns the next integer a in the in-order traversal of the given binary tree.
     * For example, given a binary tree below,
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     * the outputs will be 1, 2, 3, 4, 5, 6, 7.
     */
    public TreeNode next() {
        if (!hasNext()) {
            return null;
        }

        TreeNode res = stack.pop();
        pushLeftChildren(res.getRightNode());

        return res;
    }

}
