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
import ar.com.profebot.resolutor.utils.TreeUtils;

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
    public void stepThrough_ok2()  throws InvalidExpressionException {
        String expression = "5*0 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertTrue(listaNodos.size() == 1);
        Assert.assertTrue(estadoSimple.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO);
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
    public void step_ok2() throws InvalidExpressionException {
        String expression = "5 * 0 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.step(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO);
    }

    @Test
    public void arithmeticSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.arithmeticSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void arithmeticSearch_ok2() throws InvalidExpressionException{
        String expression = "3*5*2 = 30";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.arithmeticSearch(flattedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC);
    }

    @Test
    public void basicSearch_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.basicSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void basicSearch_ok2() throws InvalidExpressionException {
        String expression = "5*0 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.basicSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO);
    }

    @Test
    public void breakUpNumeratorSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.breakUpNumeratorSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void breakUpNumeratorSearch_ok2() throws InvalidExpressionException{
        String expression = "(2+X)/5 = 25";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.breakUpNumeratorSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.BREAK_UP_FRACTION);
    }

    @Test
    public void collectAndCombineSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.collectAndCombineSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void collectAndCombineSearch_ok2() throws InvalidExpressionException{
        String expression = "2X + 4X + X = 7";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_POLYNOMIAL_TERMS);
    }

    @Test
    public void distributeSearch_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.distributeSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void distributeSearch_ok2() throws InvalidExpressionException {
        String expression = "-(4*9*x^2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.distributeSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.DISTRIBUTE_NEGATIVE_ONE);
    }

    @Test
    public void divisionSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.divisionSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void divisionSearch_ok2() throws InvalidExpressionException{
        String expression = "2/(x/6) = 12";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.divisionSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_BY_INVERSE);
    }

    @Test
    public void fractionsSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.fractionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void fractionsSearch_ok2() throws InvalidExpressionException{
        String expression = "2/3 + 4/6 = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.fractionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_FRACTIONS);
    }

    @Test
    public void functionsSearch_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.functionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void multiplyFractionsSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.multiplyFractionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void multiplyFractionsSearch_ok2() throws InvalidExpressionException{
        String expression = "2 * 5/X = 10";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.multiplyFractionsSearch(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_FRACTIONS);
    }

    @Test
    public void reduceExponentByZero_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceExponentByZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void reduceExponentByZero_ok2() throws InvalidExpressionException {
        String expression = "4^0 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceExponentByZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REDUCE_EXPONENT_BY_ZERO);
    }

    @Test
    public void reduceMultiplicationByZero_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceMultiplicationByZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void reduceMultiplicationByZero_ok2() throws InvalidExpressionException{
        String expression = "0 * 5 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceMultiplicationByZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO);
    }

    @Test
    public void reduceZeroDividedByAnything_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceZeroDividedByAnything(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void reduceZeroDividedByAnything_ok2() throws InvalidExpressionException {
        String expression = " 0 / 5 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.reduceZeroDividedByAnything(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REDUCE_ZERO_NUMERATOR);
    }

    @Test
    public void removeAdditionOfZero_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeAdditionOfZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeAdditionOfZero_ok2() throws InvalidExpressionException{
        String expression = "4 + 0 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeAdditionOfZero(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_ADDING_ZERO);
    }

    @Test
    public void removeDivisionByOne_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeDivisionByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeDivisionByOne_ok2() throws InvalidExpressionException{
        String expression = " 5 / 1 = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeDivisionByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.DIVISION_BY_ONE);
    }

    @Test
    public void removeExponentBaseOne_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeExponentBaseOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeExponentBaseOne_ok2() throws InvalidExpressionException {
        String expression = "1^99 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeExponentBaseOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE);
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyDoubleUnaryMinus(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok2() throws InvalidExpressionException{
        String expression = "-(-4) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyDoubleUnaryMinus(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS);
    }

    @Test
    public void removeExponentByOne_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeExponentByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeExponentByOne_ok2() throws InvalidExpressionException{
        String expression = "(X+5)^1 = 6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeExponentByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE);
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeMultiplicationByNegativeOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok2() throws InvalidExpressionException {
        String expression = "(X + 5) * -1 = -X - 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeMultiplicationByNegativeOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_NEGATIVE_ONE);
    }

    @Test
    public void removeMultiplicationByOne_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeMultiplicationByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeMultiplicationByOne_ok2() throws InvalidExpressionException{
        String expression = "(5x+1) * 1 = 6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.removeMultiplicationByOne(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_ONE);
    }

    @Test
    public void rearrangeCoefficient_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.rearrangeCoefficient(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void rearrangeCoefficient_ok2() throws InvalidExpressionException {
        String expression = "X*5 = 5X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.rearrangeCoefficient(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REARRANGE_COEFF);
    }

    @Test
    public void addConstantFractions_ok1() throws InvalidExpressionException {
        String expression = "2/5 + 4/5 = 6/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addConstantFractions(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_FRACTIONS);
    }

    @Test
    public void addConstantFractions_ok2() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addConstantFractions(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeTerms_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeTerms(node,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeTerms_ok2() throws InvalidExpressionException {
        String expression = "2X + 4X = 6X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeTerms(node,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_POLYNOMIAL_TERMS);
    }

    @Test
    public void addLikePolynomialTerms_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikePolynomialTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikePolynomialTerms_ok2() throws InvalidExpressionException {
        String expression = "X + 2X = 3X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikePolynomialTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_POLYNOMIAL_TERMS);
    }

    @Test
    public void addLikeNthRootTerms_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeNthRootTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeNthRootTerms_ok2() throws InvalidExpressionException{
        String expression = "2X + X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeNthRootTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_NTH_ROOTS);
    }

    @Test
    public void addLikeTermNodes_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeTermNodes(node,"",null);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeTermNodes_ok2() throws InvalidExpressionException {
        String expression = "2X + X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.addLikeTermNodes(node,"",null);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_COEFFICIENT_OF_ONE);
    }

    @Test
    public void multiplyLikeTerms_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.multiplyLikeTerms(node,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void multiplyLikeTerms_ok2() throws InvalidExpressionException {
        String expression = "2X * X^2 * 5X = 10 X^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.multiplyLikeTerms(node,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_COEFFICIENTS);
    }

    @Test
    public void simplifyFractionSigns_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyFractionSigns(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyFractionSigns_ok2() throws InvalidExpressionException {
        String expression = "-3/-1 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyFractionSigns(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.CANCEL_MINUSES);
    }

    @Test
    public void simplifyFractionSigns_ok3() throws InvalidExpressionException {
        String expression = "3/-1 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyFractionSigns(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.SIMPLIFY_SIGNS);
    }

    @Test
    public void cancelLikeTerms_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.cancelLikeTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void cancelLikeTerms_ok2() throws InvalidExpressionException {
        String expression = "(2x^2 * 5) / 2x^2 = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.cancelLikeTerms(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.CANCEL_TERMS);
    }

    @Test
    public void simplifyPolynomialFraction_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyPolynomialFraction(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyPolynomialFraction_ok2() throws InvalidExpressionException {
        String expression = "10X / 5 = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        NodeStatus estado = super.simplifyPolynomialFraction(node);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.SIMPLIFY_FRACTION);
    }
}
