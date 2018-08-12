package ar.com.profebot.parser.container;

public class Tree {
    private TreeNode rootNode;

    public Tree() {
    }

    public Tree(TreeNode leftNode, TreeNode rightNode, String operator) {
        TreeNode rootNode = new TreeNode(operator);
        rootNode.setLeftNode(leftNode);
        rootNode.setRightNode(rightNode);
        this.setRootNode(rootNode);
    }

    public TreeNode getRootNode() {
        return rootNode;
    }
    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }
    public String toExpression(){
        return rootNode.toExpression();
    }
    public TreeNode getLeftNode() {
        return rootNode.getLeftNode();
    }
    public TreeNode getRightNode() {
        return rootNode.getRightNode();
    }
    public void setLeftNode(TreeNode node) {
        rootNode.setLeftNode(node);
    }
    public void setRightNode(TreeNode node) {
        rootNode.setRightNode(node);
    }
    public Boolean hasAnySymbol(){
        return this.toExpression().contains("X");
    }

    //Realiza el clon de un arbol.
    public Tree clone(){
        Tree tree = new Tree();
        tree.setRootNode(this.getRootNode().cloneDeep());
        return tree;
    }

    public String getComparator() {
        return this.getRootNode().getValue();
    }

    public void generateTwoWayLinkedTree(){
        rootNode.getLeftNode().assignParentData(rootNode, 0);
        rootNode.getRightNode().assignParentData(rootNode, 1);
    }

    @Override
    public String toString() {
        return toExpression();
    }
}
