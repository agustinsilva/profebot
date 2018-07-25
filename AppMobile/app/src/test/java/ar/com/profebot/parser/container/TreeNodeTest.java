package ar.com.profebot.parser.container;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeNodeTest {

    @Before
    public void setUp() {
        System.out.println(" Setting up TreeNode Test...");
    }

    @Test
    public void clone_ok() {
        TreeNode nodoRaiz = new TreeNode("=");
        TreeNode nodoIzquierdo = new TreeNode("X");
        TreeNode nodoDerecho = new TreeNode("2");
        nodoRaiz.setLeftNode(nodoIzquierdo);
        nodoRaiz.setRightNode(nodoDerecho);

        TreeNode clon = nodoRaiz.clone();
        Assert.assertEquals("=", clon.getValue());
        Assert.assertEquals("X", clon.getLeftNode().getValue());
        Assert.assertEquals("2", clon.getRightNode().getValue());
    }

    @Test
    public void multiplyCoefficient_ok() {
        TreeNode nodoRaiz = new TreeNode("X");
        nodoRaiz.multiplyCoefficient("3");
        Assert.assertEquals("3X" , nodoRaiz.getValue());
    }

    @Test
    public void addExponent_ok() {
        TreeNode nodoRaiz = new TreeNode("X");
        nodoRaiz.addExponent("3");
        Assert.assertEquals("X^3" , nodoRaiz.getValue());
    }
}
