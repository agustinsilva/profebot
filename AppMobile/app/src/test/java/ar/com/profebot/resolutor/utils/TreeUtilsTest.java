package ar.com.profebot.resolutor.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;

public class TreeUtilsTest {

    @Before
    public void setUp() {
        System.out.println(" Setting up Resolutor Tree Utils Test...");
    }

    @Test
    public void isConstant_ok1() {
        TreeNode nodoRaiz = new TreeNode("5");
        Assert.assertTrue(TreeUtils.isConstant(nodoRaiz));
    }

    @Test
    public void isConstant_error1() {
        TreeNode nodoRaiz = new TreeNode("X");
        Assert.assertFalse(TreeUtils.isConstant(nodoRaiz));
    }

    @Test
    public void hasValue_ok() {
        TreeNode nodoRaiz = new TreeNode("5");
        Assert.assertTrue(TreeUtils.hasValue(nodoRaiz,"5"));
    }

    @Test
    public void zeroValue_ok() {
        TreeNode nodoRaiz = new TreeNode("0");
        Assert.assertTrue(TreeUtils.zeroValue(nodoRaiz));
    }

    @Test
    public void isPolynomialTerm_ok() {
        TreeNode nodoRaiz = new TreeNode("3X");
        Assert.assertTrue(TreeUtils.isPolynomialTerm(nodoRaiz));
    }

    @Test
    public void isConstantFraction_ok1() throws InvalidExpressionException {
        String expression = "3/2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isConstantFraction(node));
    }

    @Test
    public void isConstantFraction_error1() throws InvalidExpressionException {
        String expression = "3/X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.isConstantFraction(node));
    }

    @Test
    public void isConstantFraction_error2() throws InvalidExpressionException {
        String expression = "X/(-2)=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.isConstantFraction(node));
    }

    @Test
    public void isNegative_ok1() {
        TreeNode nodoRaiz = new TreeNode("-2");
        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void isNegative_ok2() {
        TreeNode nodoRaiz = new TreeNode("-2X");
        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void isNegative_ok3() throws InvalidExpressionException {
        String expression = "-6/2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isNegative(node));
    }

    @Test
    public void isNegative_ok4() throws InvalidExpressionException {
        String expression = "6/(-2)=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isNegative(node));
    }

    @Test
    public void isConstantOrConstantFraction_ok1() throws InvalidExpressionException {
        String expression = "6/(-2)=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(node));
    }

    @Test
    public void isConstantOrConstantFraction_ok2() {
        TreeNode nodoRaiz = new TreeNode("3");
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isConstantOrConstantFraction_ok3() {
        TreeNode nodoRaiz = new TreeNode("-3");
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isConstantOrConstantFraction_ok4() throws InvalidExpressionException {
        String expression = "6/2=0";
        Tree tree = new ParserService().parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(node));
    }

    @Test
    public void isConstantOrConstantFraction_error1() throws InvalidExpressionException {
        String expression = "X/2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(node));
    }

    @Test
    public void isConstantOrConstantFraction_error2(){
        TreeNode nodoRaiz = new TreeNode("X");
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isIntegerFraction_ok1() throws InvalidExpressionException {
        String expression = "4/2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(node));
    }

    @Test
    public void isIntegerFraction_error1() throws InvalidExpressionException {
        String expression = "4X/2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(node));
    }

    @Test
    public void isIntegerFraction_error2() throws InvalidExpressionException {
        String expression = "4/2X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(node));
    }

    @Test
    public void flattenOperands_ok1() throws InvalidExpressionException {
        String expression = "3+4+2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(node,"+"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2),"2"));
    }

    @Test
    public void flattenOperands_ok2() throws InvalidExpressionException {
        String expression = "3*4*2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(node,"*"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2),"2"));
    }

    @Test
    public void flattenOperands_ok3() throws InvalidExpressionException {
        String expression = "3+4+5+6=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(node, "+"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0), "3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1), "4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2), "5"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(3), "6"));
    }

    @Test
    public void flattenOperands_ok4() throws InvalidExpressionException {
        String expression = "3+4-2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(node,"+"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2),"-2"));

    }

    @Test
    public void negate_ok() throws InvalidExpressionException {
        String expression = "3/2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado.getLeftNode(),"-3"));
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado.getRightNode(),"2"));
    }

    @Test
    public void negate_ok2() throws InvalidExpressionException {
        String expression = "2X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        boolean value = nodoNegado.getCoefficient() == -2;
        Assert.assertTrue(value);
    }

    @Test
    public void negate_ok3(){
        TreeNode nodoRaiz = new TreeNode("-");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        nodoRaiz.setUnaryMinus(true);
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        TreeNode nodoNegado = TreeUtils.negate(nodoRaiz);
        Assert.assertEquals(nodoRaiz.getLeftNode(),nodoNegado);
    }

    @Test
    public void negate_ok4(){
        TreeNode nodoRaiz = new TreeNode("4");
        TreeNode nodoNegado = TreeUtils.negate(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado,"-4"));
    }

    @Test
    public void negate_ok5(){
        TreeNode nodoRaiz = new TreeNode("-4");
        TreeNode nodoNegado = TreeUtils.negate(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado,"4"));
    }

    @Test
    public void negate_ok6() throws InvalidExpressionException {
        String expression = "X+2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado,"-"));
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado.getLeftNode(),"+"));
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado.getLeftNode().getLeftNode(),"X"));
        Assert.assertTrue(TreeUtils.hasValue(nodoNegado.getLeftNode().getRightNode(),"2"));
    }

    @Test
    public void negate_ok7(){
        TreeNode nodoRaiz = new TreeNode("X");
        TreeNode nodoNegado = TreeUtils.negate(nodoRaiz);
        boolean value = nodoNegado.getCoefficient() == -1;
        Assert.assertTrue(value);
    }

    @Test
    public void negate_ok8() throws InvalidExpressionException {
        String expression = "X^2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        boolean value = nodoNegado.getCoefficient() == -1 && nodoNegado.getExponent() == 2;
        Assert.assertTrue(value);
    }

    @Test
    public void negate_ok9() throws InvalidExpressionException {
        String expression = "-X^2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        boolean value = nodoNegado.getCoefficient() == 1 && nodoNegado.getExponent() == 2;
        Assert.assertTrue(value);
    }
    @Test
    public void canRearrangeCoefficient_ok() throws InvalidExpressionException {
        String expression = "X*3=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.canRearrangeCoefficient(node));
    }

    @Test
    public void canRearrangeCoefficient_ok2() throws InvalidExpressionException {
        String expression = "3/X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canRearrangeCoefficient(node));
    }

    @Test
    public void canRearrangeCoefficient_ok3() throws InvalidExpressionException {
        String expression = "3+X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canRearrangeCoefficient(node));
    }

    @Test
    public void canRearrangeCoefficient_ok4() throws InvalidExpressionException {
        String expression = "3*X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canRearrangeCoefficient(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok() throws InvalidExpressionException {
        String expression = "3/4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok2() throws InvalidExpressionException {
        String expression = "3X*4X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok3() throws InvalidExpressionException {
        String expression = "3*3=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok4() throws InvalidExpressionException {
        String expression = "3*3^2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok5() throws InvalidExpressionException {
        String expression = "3*4^2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok6() throws InvalidExpressionException {
        String expression = "3^2*3^5=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok7() throws InvalidExpressionException {
        String expression = "2^3*3^2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok8() throws InvalidExpressionException {
        String expression = "10^3*10^2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }
    @Test
    public void canMultiplyLikeTermConstantNodes_ok9() throws InvalidExpressionException {
        String expression = "10^2*10*10^4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.canMultiplyLikeTermConstantNodes(node));
    }

    @Test
    public void isFraction_ok() throws InvalidExpressionException {
        String expression = "3/4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.isFraction(node));
    }

    @Test
    public void isFraction_false() throws InvalidExpressionException {
        String expression = "3*4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.isFraction(node));
    }

    @Test
    public void getFractions_ok() throws InvalidExpressionException {
        String expression = "3/4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode fractionNode = TreeUtils.getFraction(node);
        Assert.assertTrue(TreeUtils.hasValue(fractionNode,"/"));
        Assert.assertTrue(TreeUtils.hasValue(fractionNode.getLeftNode(),"3"));
        Assert.assertTrue(TreeUtils.hasValue(fractionNode.getRightNode(),"4"));
    }

    @Test(expected = Error.class)
    public void getFractions_error() throws InvalidExpressionException {
        String expression = "3*4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeUtils.getFraction(node);
    }

    @Test
    public void hasPolynomialInDenominator_ok() throws InvalidExpressionException {
        String expression = "3/4X=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
         Assert.assertTrue(TreeUtils.hasPolynomialInDenominator(node));
    }

    @Test
    public void hasPolynomialInDenominator_error1(){
        TreeNode nodoRaiz = new TreeNode("3");
        Assert.assertFalse(TreeUtils.hasPolynomialInDenominator(nodoRaiz));
    }

    @Test
    public void hasPolynomialInDenominator_error2() throws InvalidExpressionException {
        String expression = "3X/4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertFalse(TreeUtils.hasPolynomialInDenominator(node));
    }
}
