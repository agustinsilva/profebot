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
    public void flattenOperands_ok5() throws InvalidExpressionException {
        String expression = "2+2=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"2"));
    }

    @Test
    public void flattenOperands_ok6() throws InvalidExpressionException {
        String expression = "2+2+7=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"7"));
    }

    @Test
    public void flattenOperands_ok7() throws InvalidExpressionException {
        String expression = "5*(2+3+2)*10=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"5"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(0),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(1),"3"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(2),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"10"));
    }

    @Test
    public void flattenOperands_ok8() throws InvalidExpressionException {
        String expression = "9*8*6+3+4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(0),"9"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(1),"8"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(2),"6"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"3"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"4"));
    }

    @Test
    public void flattenOperands_ok9() throws InvalidExpressionException {
        String expression = "9X*8*6+3+4=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(0),"9X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(1),"8"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(2),"6"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"3"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"4"));
    }

    @Test
    public void flattenOperands_ok10() throws InvalidExpressionException {
        String expression = "3x*4x=0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"3X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"4X"));
    }

    @Test
    public void flattenOperands_ok11() throws InvalidExpressionException {
        String expression = "2 * X / 4 * 6  =0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"/"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(0),"2X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0).getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"6"));
    }

    @Test
    public void flattenOperands_ok12() throws InvalidExpressionException {
        String expression = "2*3/4/5*6  =0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"/"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(2),"5"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"6"));
    }

    @Test
    public void flattenOperands_ok13() throws InvalidExpressionException {
        String expression = "X / (4 * X) / 8  =0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"4X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"8"));
    }

    @Test
    public void flattenOperands_ok14() throws InvalidExpressionException {
        String expression = "2X * 4X / 8  =0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"*"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"2X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"/"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(0),"4X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1).getChild(1),"8"));
    }

    @Test
    public void flattenOperands_ok15() throws InvalidExpressionException {
        String expression = "1 + 2 - 3 - 4 + 5  =0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"1"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"2"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(2),"-3"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(3),"-4"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(4),"5"));
    }

    @Test
    public void flattenOperands_ok16() throws InvalidExpressionException {
        String expression = "x - 3 = 0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode flattenedNode = TreeUtils.flattenOperands(tree.getRootNode().getLeftNode());
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode,"+"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(0),"X"));
        Assert.assertTrue(TreeUtils.hasValue(flattenedNode.getChild(1),"-3"));
    }

    @Test
    public void negate_ok() throws InvalidExpressionException {
        String expression = "1 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("-1",nodoNegado.toExpression());
    }

    @Test
    public void negate_ok2() throws InvalidExpressionException {
        String expression = "-1 = -1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("1",nodoNegado.toExpression());
    }

    @Test
    public void negate_ok3() throws InvalidExpressionException {
        String expression = "1/2 = 1/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("-1/2",nodoNegado.toExpression());
    }

    @Test
    public void negate_ok4() throws InvalidExpressionException {
        String expression = "(X + 2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("-(X+2)",nodoNegado.toExpression());
    }

    @Test
    public void negate_ok5() throws InvalidExpressionException {
        String expression = "X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("-X",nodoNegado.toExpression());
    }

    @Test
    public void negate_ok6() throws InvalidExpressionException {
        String expression = "X^2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("-X^2",nodoNegado.toExpression());
    }

    @Test
    public void negate_ok7() throws InvalidExpressionException {
        String expression = "(2/3) * X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode nodoNegado = TreeUtils.negate(node);
        Assert.assertEquals("-2/3X",nodoNegado.toExpression());
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
    public void canRearrangeCoefficient_ok5() throws InvalidExpressionException {
        String expression = "X^3 * 7 =0";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        Assert.assertTrue(TreeUtils.canRearrangeCoefficient(node));
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

    @Test
    public void getLastSymbolTerm_ok1() throws InvalidExpressionException {
        String expression = "1/X = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastSymbolTerm(node,"X");
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("1/X",outputExpression);
    }

    @Test
    public void getLastSymbolTerm_ok2() throws InvalidExpressionException {
        String expression = " 1/3x = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastSymbolTerm(node,"X");
        String outputString = outputNode.toExpression();
        Assert.assertEquals("1/3X",outputString);
    }

    @Test
    public void getLastSymbolTerm_ok3() throws InvalidExpressionException {
        String expression = " 3x = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastSymbolTerm(node,"X");
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("3X",outputExpression);
    }

    @Test
    public void getLastSymbolTerm_ok4() throws InvalidExpressionException {
        String expression = " x + 3x + 2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastSymbolTerm(node,"X");
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("3X",outputExpression);
    }

    @Test
    public void getLastSymbolTerm_ok5() throws InvalidExpressionException {
        String expression = " x/(x+3) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastSymbolTerm(node,"X");
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("X/(X+3)",outputExpression);
    }

    @Test
    public void getLastNonSymbolTerm_ok1() throws InvalidExpressionException {
        String expression = " 4x^2 + 2x + 2/4 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastNonSymbolTerm(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("2/4",outputExpression);
    }

    @Test
    public void getLastNonSymbolTerm_ok2() throws InvalidExpressionException {
        String expression = " 4x^2 + 2/4 + x = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastNonSymbolTerm(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("2/4",outputExpression);
    }

    @Test
    public void getLastNonSymbolTerm_ok3() throws InvalidExpressionException {
        String expression = " 4x^2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.getLastNonSymbolTerm(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("4",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok1() throws InvalidExpressionException {
        String expression = " (X+4) + 12 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("X + 4 + 12",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok2() throws InvalidExpressionException {
        String expression = " -(X+4X) + 12 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("-(X + 4X) + 12",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok3() throws InvalidExpressionException {
        String expression = " X + (12) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("X + 12",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok4() throws InvalidExpressionException {
        String expression = " X + (X) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("X + X",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok5() throws InvalidExpressionException {
        String expression = " X + -(X) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("X - X",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok6() throws InvalidExpressionException {
        String expression = " ((3 - 5)) * x = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("(3 - 5) * x",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok7() throws InvalidExpressionException {
        String expression = " (((-5))) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("-5",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok8() throws InvalidExpressionException {
        String expression = " ((4+5)) + ((2^3)) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("(4 + 5) + 2^3",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok9() throws InvalidExpressionException {
        String expression = " (2X^6 + -50 X^2) - (X^4) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("2X^6 - 50X^2 - X^4", outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok10() throws InvalidExpressionException {
        String expression = " (X+4) - (12 + X) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("X + 4 - (12 + X)",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok11() throws InvalidExpressionException {
        String expression = " (2X)^2 = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("(2X)^2",outputExpression);
    }

    @Test
    public void removeUnnecessaryParens_ok12() throws InvalidExpressionException {
        String expression = " ((4+x)-5)^(2) = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode outputNode = TreeUtils.removeUnnecessaryParens(node);
        String outputExpression = outputNode.toExpression();
        Assert.assertEquals("(4 + X - 5)^2",outputExpression);
    }

    @Test
    public void isQuadratic_ok1() throws InvalidExpressionException {
        String expression = " 2 + 2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok2() throws InvalidExpressionException {
        String expression = " X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok3() throws InvalidExpressionException {
        String expression = " X^2 - 4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok4() throws InvalidExpressionException {
        String expression = " X^2 + 2X + 1 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok5() throws InvalidExpressionException {
        String expression = " X^2 - 2x + 1 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok6() throws InvalidExpressionException {
        String expression = " X^2 + 3X + 2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok7() throws InvalidExpressionException {
        String expression = " X^2 - 3X + 2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok8() throws InvalidExpressionException {
        String expression = " X^2 + X - 2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok9() throws InvalidExpressionException {
        String expression = " X^2 + X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok10() throws InvalidExpressionException {
        String expression = " X^2 + 4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok11() throws InvalidExpressionException {
        String expression = " X^2 + 4X + 1 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok12() throws InvalidExpressionException {
        String expression = " X^2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok13() throws InvalidExpressionException {
        String expression = " X^3 + X^2 + X + 1 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void isQuadratic_ok14() throws InvalidExpressionException {
        String expression = " X^2 + 4 + 2^X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(TreeUtils.isQuadratic(flattenedNode));
    }

    @Test
    public void resolvesToConstant_ok1() throws InvalidExpressionException {
        String expression = " (2+2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.resolvesToConstant(flattenedNode));
    }

    @Test
    public void resolvesToConstant_ok2() throws InvalidExpressionException {
        String expression = " 10 = 10";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.resolvesToConstant(flattenedNode));
    }

    @Test
    public void resolvesToConstant_ok3() throws InvalidExpressionException {
        String expression = " ((2^2 + 4)) * 7 / 8 = 7";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.resolvesToConstant(flattenedNode));
    }

    @Test
    public void resolvesToConstant_ok4() throws InvalidExpressionException {
        String expression = " 2 * 3^X = 7";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertFalse(TreeUtils.resolvesToConstant(flattenedNode));
    }

    @Test
    public void resolvesToConstant_ok5() throws InvalidExpressionException {
        String expression = " -(2) * -3 = 6";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        Assert.assertTrue(TreeUtils.resolvesToConstant(flattenedNode));
    }
}
