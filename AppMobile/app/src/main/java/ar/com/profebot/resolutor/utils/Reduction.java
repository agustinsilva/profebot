package ar.com.profebot.resolutor.utils;

import ar.com.profebot.parser.container.TreeNode;

/**
 * Reduction Class to be used in InvalidOptionService
 **/
public class Reduction {

    TreeNode treeNode;
    ReductionType reductionType;

    public Reduction(TreeNode treeNode, ReductionType reductionType) {
        this.treeNode = treeNode;
        this.reductionType = reductionType;
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public ReductionType getReductionType() {
        return reductionType;
    }

    public void setReductionType(ReductionType reductionType) {
        this.reductionType = reductionType;
    }

    public enum ReductionType {
        OPERACIONES_BASICAS,
        DISTRIBUTIVA,
        ASOCIATIVA,
        POTENCIA_DE_BINOMIO;
    }
}