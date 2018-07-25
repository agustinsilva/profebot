package ar.com.profebot.resolutor.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.parser.container.TreeNode;

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
    public void isConstantFraction_ok1() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("3");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setRightNode(nodoDerecho);
        nodoRaiz.setLeftNode(nodoIzquierdo);
        Assert.assertTrue(TreeUtils.isConstantFraction(nodoRaiz));
    }

    @Test
    public void isConstantFraction_error1() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("3");
        TreeNode nodoDerecho = new TreeNode("X");
        Assert.assertFalse(TreeUtils.isConstantFraction(nodoRaiz));
    }

    @Test
    public void isConstantFraction_error2() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("X");
        TreeNode nodoDerecho = new TreeNode("-2");
        Assert.assertFalse(TreeUtils.isConstantFraction(nodoRaiz));
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
    public void isNegative_ok3() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("-6");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);

        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void isNegative_ok4() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("6");
        TreeNode nodoDerecho = new TreeNode("-2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);

        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void isConstantOrConstantFraction_ok1() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("6");
        TreeNode nodoDerecho = new TreeNode("-2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);

        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
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
    public void isConstantOrConstantFraction_ok4(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("6");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isConstantOrConstantFraction_error1(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("X");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isConstantOrConstantFraction_error2(){
        TreeNode nodoRaiz = new TreeNode("X");
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isIntegerFraction_ok1(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("4");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);
        Assert.assertTrue(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isIntegerFraction_error1(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("4X");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void isIntegerFraction_error2(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("4");
        TreeNode nodoDerecho = new TreeNode("2X");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);
        Assert.assertFalse(TreeUtils.isConstantOrConstantFraction(nodoRaiz));
    }

    @Test
    public void flattenOperands_ok1(){
        TreeNode nodoRaiz = new TreeNode("+");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("+");
        TreeNode nodoIzquierdoSuma = new TreeNode("4");
        TreeNode nodoDerechoSuma = new TreeNode("2");
        nodoDerechoRaiz.setLeftNode(nodoIzquierdoSuma);
        nodoDerechoRaiz.setRightNode(nodoDerechoSuma);
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        TreeNode node = TreeUtils.flattenOperands(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(node,"+"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2),"2"));
    }

    @Test
    public void flattenOperands_ok2(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoSuma = new TreeNode("4");
        TreeNode nodoDerechoSuma = new TreeNode("2");
        nodoDerechoRaiz.setLeftNode(nodoIzquierdoSuma);
        nodoDerechoRaiz.setRightNode(nodoDerechoSuma);
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        TreeNode node = TreeUtils.flattenOperands(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(node,"*"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2),"2"));
    }

    @Test
    public void flattenOperands_ok3() {
        TreeNode nodoRaiz = new TreeNode("+");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("+");
        TreeNode nodoIzquierdoSuma1 = new TreeNode("4");
        TreeNode nodoDerechoSuma1 = new TreeNode("+");
        TreeNode nodoDerechoSuma2 = new TreeNode("5");
        TreeNode nodoIzquierdoSuma2 = new TreeNode("6");
        nodoDerechoSuma1.setLeftNode(nodoDerechoSuma2);
        nodoDerechoSuma1.setRightNode(nodoIzquierdoSuma2);
        nodoDerechoRaiz.setLeftNode(nodoIzquierdoSuma1);
        nodoDerechoRaiz.setRightNode(nodoDerechoSuma1);
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        TreeNode node = TreeUtils.flattenOperands(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(node, "+"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0), "3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1), "4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2), "5"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(3), "6"));
    }

    @Test
    public void flattenOperands_ok4(){
        TreeNode nodoRaiz = new TreeNode("+");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("-");
        TreeNode nodoIzquierdoSuma = new TreeNode("4");
        TreeNode nodoDerechoSuma = new TreeNode("2");
        nodoDerechoRaiz.setLeftNode(nodoIzquierdoSuma);
        nodoDerechoRaiz.setRightNode(nodoDerechoSuma);
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        TreeNode node = TreeUtils.flattenOperands(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(node,"+"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(0),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(1),"4"));
        Assert.assertTrue(TreeUtils.hasValue(node.getChild(2),"-2"));
    }

    @Test
    public void canRearrangeCoefficient_ok(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("X");
        TreeNode nodoDerechoRaiz = new TreeNode("3");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertTrue(TreeUtils.canRearrangeCoefficient(nodoRaiz));
    }

    @Test
    public void canRearrangeCoefficient_ok2(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("X");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.canRearrangeCoefficient(nodoRaiz));
    }

    @Test
    public void canRearrangeCoefficient_ok3(){
        TreeNode nodoRaiz = new TreeNode("+");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("X");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.canRearrangeCoefficient(nodoRaiz));
    }

    @Test
    public void canRearrangeCoefficient_ok4(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("X");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.canRearrangeCoefficient(nodoRaiz));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("4");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(nodoRaiz));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok2(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3X");
        TreeNode nodoDerechoRaiz = new TreeNode("4X");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(nodoRaiz));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok3(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("3");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(nodoRaiz));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok4(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("^");
        TreeNode nodoIzquierdoPotencia = new TreeNode("3");
        TreeNode nodoDerechoPotencia = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        nodoDerechoRaiz.setLeftNode(nodoIzquierdoPotencia);
        nodoDerechoRaiz.setRightNode(nodoDerechoPotencia);
        Assert.assertTrue(TreeUtils.canMultiplyLikeTermConstantNodes(nodoRaiz));
    }

    @Test
    public void canMultiplyLikeTermConstantNodes_ok5(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("^");
        TreeNode nodoIzquierdoPotencia = new TreeNode("4");
        TreeNode nodoDerechoPotencia = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        nodoDerechoRaiz.setLeftNode(nodoIzquierdoPotencia);
        nodoDerechoRaiz.setRightNode(nodoDerechoPotencia);
        Assert.assertFalse(TreeUtils.canMultiplyLikeTermConstantNodes(nodoRaiz));
    }

    @Test
    public void isFraction_ok(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("4");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertTrue(TreeUtils.isFraction(nodoRaiz));
    }

    @Test
    public void isFraction_false(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("4");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.isFraction(nodoRaiz));
    }

    @Test
    public void getFractions_ok(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("4");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        TreeNode node = TreeUtils.getFraction(nodoRaiz);
        Assert.assertTrue(TreeUtils.hasValue(node,"/"));
        Assert.assertTrue(TreeUtils.hasValue(node.getLeftNode(),"3"));
        Assert.assertTrue(TreeUtils.hasValue(node.getRightNode(),"4"));
    }

    @Test(expected = Error.class)
    public void getFractions_error(){
        TreeNode nodoRaiz = new TreeNode("*");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("4");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        TreeUtils.getFraction(nodoRaiz);
    }

    @Test
    public void hasPolynomialInDenominator_ok(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3");
        TreeNode nodoDerechoRaiz = new TreeNode("4X");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertTrue(TreeUtils.hasPolynomialInDenominator(nodoRaiz));
    }

    @Test
    public void hasPolynomialInDenominator_error1(){
        TreeNode nodoRaiz = new TreeNode("3");
        Assert.assertFalse(TreeUtils.hasPolynomialInDenominator(nodoRaiz));
    }

    @Test
    public void hasPolynomialInDenominator_error2(){
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdoRaiz = new TreeNode("3X");
        TreeNode nodoDerechoRaiz = new TreeNode("4");
        nodoRaiz.setLeftNode(nodoIzquierdoRaiz);
        nodoRaiz.setRightNode(nodoDerechoRaiz);
        Assert.assertFalse(TreeUtils.hasPolynomialInDenominator(nodoRaiz));
    }
}
