package ar.com.profebot.resolutor.container;

import ar.com.profebot.parser.container.Tree;

public class ResolutionStep {
    Integer type;
    Tree tree;

    public ResolutionStep(Integer type, Tree tree) {
        this.type = type;
        this.tree = tree;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}
