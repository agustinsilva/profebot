package ar.com.profebot.resolutor;

import android.support.v4.app.INotificationSideChannel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.resolutor.service.SimplifyService;
import ar.com.profebot.parser.service.ParserService;
import ar.com.profebot.resolutor.container.NodeStatus;

public class SimplifyServiceTest extends SimplifyService {

    @Before
    public void setUp() {
        System.out.println(" Setting up Resolutor Test...");
    }

    @Test
    public void stepThrough_ok1()  throws InvalidExpressionException {
        String expression = "(2+2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertTrue(listaNodos.size() == 1);
        Assert.assertTrue(estadoSimple.getChangeType() == NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC);
    }

    @Test
    public void step_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.step(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void arithmeticSearch() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.arithmeticSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void basicSearch() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.basicSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void breakUpNumeratorSearch() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.breakUpNumeratorSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void collectAndCombineSearch() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.collectAndCombineSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void distributeSearch() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.distributeSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void divisionSearch() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.divisionSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void fractionsSearch() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.fractionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void functionsSearch() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.functionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void multiplyFractionsSearch() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.multiplyFractionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void reduceExponentByZero() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceExponentByZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void reduceMultiplicationByZero() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceMultiplicationByZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void reduceZeroDividedByAnything() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceZeroDividedByAnything(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeAdditionOfZero() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeAdditionOfZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeDivisionByOne() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeDivisionByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeExponentBaseOne() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeExponentBaseOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyDoubleUnaryMinus() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyDoubleUnaryMinus(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeExponentByOne() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeExponentByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeMultiplicationByNegativeOne() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeMultiplicationByNegativeOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeMultiplicationByOne() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeMultiplicationByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void rearrangeCoefficient() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.rearrangeCoefficient(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addConstantAndFraction() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addConstantAndFraction(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addConstantFractions() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addConstantFractions(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeTerms() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeTerms(node,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikePolynomialTerms() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikePolynomialTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeNthRootTerms() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeNthRootTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeTermNodes() throws InvalidExpressionException {
        String expression = "2X + X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeTermNodes(node,"",null);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_COEFFICIENT_OF_ONE);
    }

    @Test
    public void multiplyLikeTerms() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.multiplyLikeTerms(node,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyFractionSigns() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyFractionSigns(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void cancelLikeTerms() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.cancelLikeTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyPolynomialFraction() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyPolynomialFraction(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

}
