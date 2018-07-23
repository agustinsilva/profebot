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
    public void esNegativo_ok1() {
        TreeNode nodoRaiz = new TreeNode("-2.0");
        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void esNegativo_ok2() {
        TreeNode nodoRaiz = new TreeNode("-2X");
        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void esNegativo_ok3() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("-6");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);

        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }

    @Test
    public void esNegativo_ok4() {
        TreeNode nodoRaiz = new TreeNode("/");
        TreeNode nodoIzquierdo = new TreeNode("6");
        TreeNode nodoDerecho = new TreeNode("-2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);

        Assert.assertTrue(TreeUtils.isNegative(nodoRaiz));
    }
}
