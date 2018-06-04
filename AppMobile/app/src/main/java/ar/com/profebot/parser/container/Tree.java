package ar.com.profebot.parser.container;

import java.util.List;

public class Tree {
    private TreeNode rootNode;
    private List<TreeNode> nodes;
    public TreeNode getRootNode() {
        return rootNode;
    }
    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }
    public List<TreeNode> getNodes() {
        return nodes;
    }
    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }
    public String toExpression(){
        return rootNode.toExpression();
    }

}
