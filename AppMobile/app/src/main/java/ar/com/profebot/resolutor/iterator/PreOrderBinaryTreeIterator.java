package ar.com.profebot.resolutor.iterator;

import java.util.ArrayDeque;

import ar.com.profebot.parser.container.TreeNode;

public class PreOrderBinaryTreeIterator {
    private ArrayDeque<TreeNode> stack = new ArrayDeque<TreeNode>();

    /** Constructor */
    public PreOrderBinaryTreeIterator(TreeNode root) {
        if (root != null) {
            stack.push(root); // add to end of queue
        }
    }

    /** Return true if traversal has not finished; otherwise, return false.
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /** Returns the next integer a in the pre-order traversal of the given binary tree.
     * For example, given a binary tree below,
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     * the outputs will be 4, 2, 1, 3, 6, 5, 7.
     */
    public TreeNode next() {
        if (!hasNext()) {
            return null;
        }

        TreeNode res = stack.pop(); // retrieve and remove the head of queue
        if (res.getRightNode() != null) stack.push(res.getRightNode());
        if (res.getLeftNode() != null) stack.push(res.getLeftNode());

        return res;
    }
}
