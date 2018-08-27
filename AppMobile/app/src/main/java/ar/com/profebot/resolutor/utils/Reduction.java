package ar.com.profebot.resolutor.utils;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.parser.container.TreeNode;

/**
 * Reduction Class to be used in InvalidOptionService
 **/
public class Reduction {

    TreeNode tree;
    ReductionType reductionType;

    public Reduction(TreeNode tree, ReductionType reductionType) {
        this.tree = tree;
        this.reductionType = reductionType;
    }

    public TreeNode getTree() {
        return tree;
    }

    public void setTree(TreeNode tree) {
        this.tree = tree;
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