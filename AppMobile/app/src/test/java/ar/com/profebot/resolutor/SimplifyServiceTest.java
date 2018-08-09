package ar.com.profebot.resolutor;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import ar.com.profebot.resolutor.container.NodeStatus;
import ar.com.profebot.resolutor.service.SimplifyService;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class SimplifyServiceTest extends SimplifyService {

    @Before
    public void setUp() {
        System.out.println(" Setting up Resolutor Test...");
    }

    @Test
    public void stepThrough_ok1()  throws InvalidExpressionException {
        String expression = "2+2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(1);
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
    public void arithmeticSearch_ok3() throws InvalidExpressionException{
        String expression = "2+2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.arithmeticSearch(flattedNode);
        Assert.assertEquals("4",estado.getNewNode().toExpression());
    }

    @Test
    public void arithmeticSearch_ok4() throws InvalidExpressionException{
        String expression = "2*3*5 = 30";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.arithmeticSearch(flattedNode);
        Assert.assertEquals("30",estado.getNewNode().toExpression());
    }

    @Test
    public void arithmeticSearch_ok5() throws InvalidExpressionException{
        String expression = "6*6 = 30";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.arithmeticSearch(flattedNode);
        Assert.assertEquals("36",estado.getNewNode().toExpression());
    }

    @Test
    public void arithmeticSearch_ok6() throws InvalidExpressionException{
        String expression = "9/4 = 9/4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.arithmeticSearch(flattedNode);
        Assert.assertEquals("9/4",estado.getNewNode().toExpression());
    }

    @Test
    public void arithmeticSearch_ok7() throws InvalidExpressionException{
        String expression = "16 - 1953125 = -1953109";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.arithmeticSearch(flattedNode);
        Assert.assertEquals("-1953109",estado.getNewNode().toExpression());
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void breakUpNumeratorSearch_ok2() throws InvalidExpressionException{
        String expression = "(2+X)/5 = 25";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.BREAK_UP_FRACTION);
    }

    @Test
    public void breakUpNumeratorSearch_ok3() throws InvalidExpressionException{
        String expression = " (X+3+X)/3 = (X / 3 + 3/3 + X / 3)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertEquals("(X / 3 + 3/3 + X / 3", estado.getNewNode().toExpression());
    }

    @Test
    public void breakUpNumeratorSearch_ok4() throws InvalidExpressionException{
        String expression = " (2+X)/4 = (2/4 + X/4)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertEquals("(2/4 + X/4)", estado.getNewNode().toExpression());
    }

    @Test
    public void breakUpNumeratorSearch_ok5() throws InvalidExpressionException{
        String expression = " 2(X+3)/3 = 2 * (X/3 + 3/3)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertEquals("2 * (X/3 + 3/3)", estado.getNewNode().toExpression());
    }

    @Test
    public void evaluateConstantSum_ok1() throws InvalidExpressionException{
        String expression = " 4/10 + 3/5 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.evaluateConstantSum(flattenedNode);
        Assert.assertEquals("1", estado.getNewNode().toExpression());
    }

    @Test
    public void evaluateConstantSum_ok2() throws InvalidExpressionException{
        String expression = " 4/5 + 3/5 + 2 = 17/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.evaluateConstantSum(flattenedNode);
        Assert.assertEquals("17/5", estado.getNewNode().toExpression());
    }

    @Test
    public void evaluateConstantSum_ok3() throws InvalidExpressionException{
        String expression = " 9 + 4/5 + 1/5 + 2 = 12";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.evaluateConstantSum(flattenedNode);
        Assert.assertEquals("12", estado.getNewNode().toExpression());
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
        String expression = "2/3 + 4/6 = 8/6";
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
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
    public void reduceExponentByZero_ok3() throws InvalidExpressionException {
        String expression = "4^0 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.reduceExponentByZero(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
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
    public void reduceMultiplicationByZero_ok3() throws InvalidExpressionException{
        String expression = "0 * 5 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.reduceMultiplicationByZero(flattenedNode);
        Assert.assertEquals("0",estado.getNewNode().toExpression());
    }

    @Test
    public void reduceMultiplicationByZero_ok4() throws InvalidExpressionException{
        String expression = "0X = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.reduceMultiplicationByZero(flattenedNode);
        Assert.assertEquals("0",estado.getNewNode().toExpression());
    }

    @Test
    public void reduceMultiplicationByZero_ok5() throws InvalidExpressionException{
        String expression = "2*0*X^2 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.reduceMultiplicationByZero(flattenedNode);
        Assert.assertEquals("0",estado.getNewNode().toExpression());
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
    public void reduceZeroDividedByAnything_ok3() throws InvalidExpressionException {
        String expression = " 0 / 5 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.reduceZeroDividedByAnything(flattenedNode);
        Assert.assertEquals("0",estado.getNewNode().toExpression());
    }

    @Test
    public void reduceZeroDividedByAnything_ok4() throws InvalidExpressionException {
        String expression = "0/(X+6+7+X^2) = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.reduceZeroDividedByAnything(flattenedNode);
        Assert.assertEquals("0",estado.getNewNode().toExpression());
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
    public void removeAdditionOfZero_ok3() throws InvalidExpressionException{
        String expression = "2+0+X = 2 + X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeAdditionOfZero(flattenedNode);
        Assert.assertEquals("2 + X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeAdditionOfZero_ok4() throws InvalidExpressionException{
        String expression = "2+X+0 = 2 + X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeAdditionOfZero(flattenedNode);
        Assert.assertEquals("2 + X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeAdditionOfZero_ok5() throws InvalidExpressionException{
        String expression = "0+2+X = 2 + X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeAdditionOfZero(flattenedNode);
        Assert.assertEquals("2 + X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeDivisionByOne_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeDivisionByOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeDivisionByOne_ok2() throws InvalidExpressionException{
        String expression = " 5 / 1 = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeDivisionByOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.DIVISION_BY_ONE);
    }

    @Test
    public void removeDivisionByOne_ok3() throws InvalidExpressionException{
        String expression = " X/1 = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeDivisionByOne(flattenedNode);
        Assert.assertEquals("X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeExponentBaseOne_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentBaseOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeExponentBaseOne_ok2() throws InvalidExpressionException {
        String expression = "1^99 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentBaseOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE);
    }

    @Test
    public void removeExponentBaseOne_ok3() throws InvalidExpressionException {
        String expression = "1^3 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentBaseOne(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    @Test
    public void removeExponentBaseOne_ok4() throws InvalidExpressionException {
        String expression = "1^(2 + 3 + 5/4 + 7 - 6/7) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentBaseOne(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyDoubleUnaryMinus(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok2() throws InvalidExpressionException{
        String expression = "-(-4) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyDoubleUnaryMinus(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS);
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok3() throws InvalidExpressionException{
        String expression = "--5 = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyDoubleUnaryMinus(flattenedNode);
        Assert.assertEquals("5",estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok4() throws InvalidExpressionException{
        String expression = "--X = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyDoubleUnaryMinus(flattenedNode);
        Assert.assertEquals("X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeExponentByOne_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentByOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeExponentByOne_ok2() throws InvalidExpressionException{
        String expression = "(X+5)^1 = 6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentByOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE);
    }

    @Test
    public void removeExponentByOne_ok3() throws InvalidExpressionException{
        String expression = "X^1 = 6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentByOne(flattenedNode);
        Assert.assertEquals("X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByNegativeOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok2() throws InvalidExpressionException {
        String expression = "(X + 5) * -1 = -X - 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByNegativeOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_NEGATIVE_ONE);
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok3() throws InvalidExpressionException {
        String expression = "-1*X = -X - 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByNegativeOne(flattenedNode);
        Assert.assertEquals("-X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok4() throws InvalidExpressionException {
        String expression = "X^2*-1 = -X - 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByNegativeOne(flattenedNode);
        Assert.assertEquals("-X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByNegativeOne_ok5() throws InvalidExpressionException {
        String expression = "2X*2*-1 = -X - 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByNegativeOne(flattenedNode);
        Assert.assertEquals("2X * 2 * -1",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByOne_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void removeMultiplicationByOne_ok2() throws InvalidExpressionException{
        String expression = "(5x+1) * 1 = 6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByOne(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_ONE);
    }

    @Test
    public void removeMultiplicationByOne_ok3() throws InvalidExpressionException{
        String expression = "X*1 = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByOne(flattenedNode);
        Assert.assertEquals("X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByOne_ok4() throws InvalidExpressionException{
        String expression = "1X = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByOne(flattenedNode);
        Assert.assertEquals("X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByOne_ok5() throws InvalidExpressionException{
        String expression = "1*X^2 = X^2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByOne(flattenedNode);
        Assert.assertEquals("X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void removeMultiplicationByOne_ok6() throws InvalidExpressionException{
        String expression = "2*1*X^2 = 2 * 1X^2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeMultiplicationByOne(flattenedNode);
        Assert.assertEquals("2 * 1X^2",estado.getNewNode().toExpression());
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
    public void rearrangeCoefficient_ok3() throws InvalidExpressionException {
        String expression = "2 * X^2 = 2X^2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.rearrangeCoefficient(flattenedNode);
        Assert.assertEquals("2X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void rearrangeCoefficient_ok4() throws InvalidExpressionException {
        String expression = "X^3 * 5 = 5X^3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.rearrangeCoefficient(flattenedNode);
        Assert.assertEquals("5X^3",estado.getNewNode().toExpression());
    }

    @Test
    public void addConstantFractions_ok1() throws InvalidExpressionException {
        String expression = "2/5 + 4/5 = 6/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantFractions(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_FRACTIONS);
    }

    @Test
    public void addConstantFractions_ok2() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantFractions(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addConstantFractions_ok3() throws InvalidExpressionException {
        String expression = "4/5 + 3/5 = 7/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantFractions(flattenedNode);
        Assert.assertEquals("7/5",estado.getNewNode().toExpression());
    }

    @Test
    public void addConstantFractions_ok4() throws InvalidExpressionException {
        String expression = "4/10 + 3/5 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantFractions(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    @Test
    public void addConstantFractions_ok5() throws InvalidExpressionException {
        String expression = "4/9 + 3/5 = 47/45";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantFractions(flattenedNode);
        Assert.assertEquals("47/45",estado.getNewNode().toExpression());
    }

    @Test
    public void addConstantFractions_ok6() throws InvalidExpressionException {
        String expression = "4/5 - 4/5 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantFractions(flattenedNode);
        Assert.assertEquals("0",estado.getNewNode().toExpression());
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addLikeTerms(flattenedNode,true);
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addLikePolynomialTerms(flattenedNode);
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
        String expression = "2X^2 + X^2 = 27";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addLikeNthRootTerms(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.ADD_NTH_ROOTS);
    }

    @Test
    public void addLikeTermNodes_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addLikeTermNodes(flattenedNode,"",null);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void addLikeTermNodes_ok2() throws InvalidExpressionException {
        String expression = "2X + X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addLikeTermNodes(flattenedNode,"",null);
        Assert.assertTrue(estado.getSubsteps().get(0).getChangeType() == NodeStatus.ChangeTypes.ADD_COEFFICIENT_OF_ONE);
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
        String expression = "X * X^2 * X = X^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyLikeTerms(flattenedNode,true);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_COEFFICIENTS);
    }

    @Test
    public void simplifyFractionSigns_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyFractionSigns(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyFractionSigns_ok2() throws InvalidExpressionException {
        String expression = "-3/-1 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyFractionSigns(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.CANCEL_MINUSES);
    }

    @Test
    public void simplifyFractionSigns_ok3() throws InvalidExpressionException {
        String expression = "3/-1 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyFractionSigns(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.SIMPLIFY_SIGNS);
    }

    @Test
    public void simplifyFractionSigns_ok4() throws InvalidExpressionException {
        String expression = "-12x / -27 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyFractionSigns(flattenedNode);
        Assert.assertEquals("12x/27",estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyFractionSigns_ok5() throws InvalidExpressionException {
        String expression = "X / -X = -X / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyFractionSigns(flattenedNode);
        Assert.assertEquals("-X/X",estado.getNewNode().toExpression());
    }

    @Test
    public void addConstantAndFraction_ok1() throws InvalidExpressionException {
        String expression = "7 + 1/2 = 15/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantAndFraction(flattenedNode);
        Assert.assertEquals("15/2",estado.getNewNode().toExpression());
    }

    @Test
    public void addConstantAndFraction_ok2() throws InvalidExpressionException {
        String expression = "5/6 + 3 = 23/6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.addConstantAndFraction(flattenedNode);
        Assert.assertEquals("23/6",estado.getNewNode().toExpression());
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void simplifyPolynomialFraction_ok2() throws InvalidExpressionException {
        String expression = "10X / 5 = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.SIMPLIFY_FRACTION);
        Assert.assertEquals("2X", estado.getNewNode().toExpression());
    }

    @Test
    public void canMultiplyLikeTermPolynomialNodes_ok1() throws InvalidExpressionException {
        String expression = "X^2 * X * X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(super.canMultiplyLikeTermPolynomialNodes(flattenedNode));
    }

    @Test
    public void canMultiplyLikeTermPolynomialNodes_ok2() throws InvalidExpressionException {
        String expression = "X^2 * 3X * X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(super.canMultiplyLikeTermPolynomialNodes(flattenedNode));
    }

    @Test
    public void canMultiplyLikeTermPolynomialNodes_ok3() throws InvalidExpressionException {
        String expression = "X * X^3 = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(super.canMultiplyLikeTermPolynomialNodes(flattenedNode));
    }

    @Test
    public void canMultiplyLikeTermPolynomialNodes_ok4() throws InvalidExpressionException {
        String expression = "X^3 * X^2 = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(super.canMultiplyLikeTermPolynomialNodes(flattenedNode));
    }

    @Test
    public void canAddLikeTermPolynomialNodes_ok1() throws InvalidExpressionException {
        String expression = "X^2 + X^2 = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(super.canAddLikeTermPolynomialNodes(flattenedNode));
    }

    @Test
    public void canAddLikeTermPolynomialNodes_ok2() throws InvalidExpressionException {
        String expression = "X + X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(super.canAddLikeTermPolynomialNodes(flattenedNode));
    }

    @Test
    public void canAddLikeTermPolynomialNodes_ok3() throws InvalidExpressionException {
        String expression = "X^3 + X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(super.canAddLikeTermPolynomialNodes(flattenedNode));
    }
}
