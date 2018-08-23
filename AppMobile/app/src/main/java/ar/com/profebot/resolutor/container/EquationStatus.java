package ar.com.profebot.resolutor.container;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;

public class EquationStatus {

    private NodeStatus.ChangeTypes changeType;
    private Tree oldEquation;
    private Tree newEquation;
    private List<EquationStatus> substeps;


    public EquationStatus(NodeStatus.ChangeTypes changeType, Tree oldEquation, Tree newEquation) {
        this(changeType, oldEquation, newEquation, null);
    }

    public EquationStatus(NodeStatus.ChangeTypes changeType, Tree oldEquation, Tree newEquation, List<EquationStatus> substeps) {
        this.changeType = changeType;
        this.oldEquation = oldEquation;
        this.newEquation = newEquation;
        this.substeps = substeps;
    }

    public Boolean hasChanged(){
        return !NodeStatus.ChangeTypes.NO_CHANGE.equals(this.changeType);
    }
    public static EquationStatus noChange(Tree equation) {
        return new EquationStatus(NodeStatus.ChangeTypes.NO_CHANGE, null, equation, null);
    };

    public Tree getOldEquation() {
        return oldEquation;
    }

    public Tree getNewEquation() {
        return newEquation;
    }

    public NodeStatus.ChangeTypes getChangeType() {
        return changeType;
    }

    public List<EquationStatus> getSubsteps() {
        return substeps;
    }

    public static Tree resetChangeGroups(Tree equation) {
        TreeNode leftNode = NodeStatus.resetChangeGroups(equation.getLeftNode());
        TreeNode rightNode = NodeStatus.resetChangeGroups(equation.getRightNode());

        TreeNode rootNode = new TreeNode(equation.getRootNode().getValue());
        rootNode.setLeftNode(leftNode);
        rootNode.setRightNode(rightNode);

        Tree newEquation = new Tree();
        newEquation.setRootNode(rootNode);

        return newEquation;
    }

    public static EquationStatus addLeftStep(Tree equation, NodeStatus leftStep) {
        List<EquationStatus> substeps = new ArrayList<>();
        if (leftStep.getSubsteps() != null) {
            for (NodeStatus substep : leftStep.getSubsteps()) {
                substeps.add(EquationStatus.addLeftStep(equation, substep));
            }
        }

        Tree oldEquation = null;
        if (leftStep.getOldNode() != null) {
            oldEquation = equation.clone();
            oldEquation.setLeftNode(leftStep.getOldNode());
        }

        Tree newEquation = equation.clone();
        newEquation.setLeftNode(leftStep.getNewNode());
        return new EquationStatus(
                leftStep.getChangeType(), oldEquation, newEquation, substeps);
    }

    public static EquationStatus addRightStep(Tree equation, NodeStatus rightStep) {
        List<EquationStatus> substeps = new ArrayList<>();

        if (rightStep.getSubsteps() != null) {
            for (NodeStatus substep : rightStep.getSubsteps()) {
                substeps.add(EquationStatus.addRightStep(equation, substep));
            }
        }

        Tree oldEquation = null;
        if (rightStep.getOldNode() != null) {
            oldEquation = equation.clone();
            oldEquation.setRightNode(rightStep.getOldNode());
        }
        Tree newEquation = equation.clone();
        newEquation.setRightNode(rightStep.getNewNode());
        return new EquationStatus(
                rightStep.getChangeType(), oldEquation, newEquation, substeps);
    }

    @Override
    public String toString() {
        return "EquationStatus{" +
                "changeType=" + changeType +
                (newEquation!=null? ", newEquation=" + newEquation.toExpression(): "") +
                '}';
    }

    public String getUIDescription() {
        // TODO Generar la prosa dado el tipo de cambio
        return "Pendiente";
    }
}
