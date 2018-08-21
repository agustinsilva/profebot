package ar.com.profebot.resolutor;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
        NodeStatus estadoSimple = listaNodos.get(0);
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
    public void stepThrough_ok3()  throws InvalidExpressionException {
        String expression = "2+2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertTrue(listaNodos.size() == 1);
        Assert.assertEquals("4",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok4()  throws InvalidExpressionException {
        String expression = "(2+2)*5 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("4*5",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok5()  throws InvalidExpressionException {
        String expression = "(2+2)*5 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("4*5",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok6()  throws InvalidExpressionException {
        String expression = "5*(2+2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("5*4",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok7()  throws InvalidExpressionException {
        String expression = "2*(2+2) + 2^3 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("2*4+2^3",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok8()  throws InvalidExpressionException {
        String expression = "6*6 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("36",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok9()  throws InvalidExpressionException {
        String expression = "2+X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        Assert.assertEquals(0,listaNodos.size());
    }

    @Test
    public void stepThrough_ok10()  throws InvalidExpressionException {
        String expression = "(2+2)*X = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("4X",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok11()  throws InvalidExpressionException {
        String expression = "(2+2)*X+3 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("4X+3",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok12()  throws InvalidExpressionException {
        String expression = "2+X+7 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("X+9",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok13()  throws InvalidExpressionException {
        String expression = "(X + X) + (X^2 + X^2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("2X^2+2X",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok14()  throws InvalidExpressionException {
        String expression = "10 + (X^2 + X^2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("2X^2+10",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok15()  throws InvalidExpressionException {
        String expression = "10X^2 + 1/2 * X^2 + 3/2 * X^2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("12X^2",estadoSimple.getNewNode().toExpression());
    }

   /* @Test
    public void stepThrough_ok16()  throws InvalidExpressionException {
        String expression = "2X^(2+1)= 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("2X^3",estadoSimple.getNewNode().toExpression());
    }*/

    @Test
    public void stepThrough_ok17()  throws InvalidExpressionException {
        String expression = "2 + 5/2 + 3= 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estadoSimple = listaNodos.get(0);
        Assert.assertEquals("5+5/2",estadoSimple.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok18()  throws InvalidExpressionException {
        String expression = "(2+2)*5 = 20";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("20",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok19()  throws InvalidExpressionException {
        String expression = "(8+(-4))*5 = 20";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("20",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok20()  throws InvalidExpressionException {
        String expression = "5*(2+2)*10 = 200";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("200",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok21()  throws InvalidExpressionException {
        String expression = "(2+(2)+7) = 11";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("11",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok22()  throws InvalidExpressionException {
        String expression = "(8-2) * 2^2 * (1+1) / (4 /2) / 5 = 24/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("24/5",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok23()  throws InvalidExpressionException {
        String expression = "X^2 + 3X*(-4X) + 5X^3 + 3X^2 + 6 = 24/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("5X^3-8X^2+6",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok24()  throws InvalidExpressionException {
        String expression = "(2X^2 - 4) + (4X^2 + 3) = 24/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("6X^2-1",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok25()  throws InvalidExpressionException {
        String expression = "(2X^1 + 4) + (4X^2 + 3) = 24/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("4X^2+2X+7",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok26()  throws InvalidExpressionException {
        String expression = "X^2 + 3X*(-4X) + 5X^3 + 3X^2 + 6= 24/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("5X^3-8X^2+6",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok27()  throws InvalidExpressionException {
        String expression = "2 * 4 / 5 * 10 + 3 = 19";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("19",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok28()  throws InvalidExpressionException {
        String expression = "2X * 5X / 2 = 19";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("5X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok29()  throws InvalidExpressionException {
        String expression = "2X * 4X / 5 * 10 + 3= 19";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("16X^2+3",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok30()  throws InvalidExpressionException {
        String expression = "2X * 4X / 2 / 4 = 19";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok31()  throws InvalidExpressionException {
        String expression = "2X * 4X / 5 * 10 + 3 = 19";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("16X^2+3",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok32()  throws InvalidExpressionException {
        String expression = "2X/X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok33()  throws InvalidExpressionException {
        String expression = "2X/4/3 = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X/6",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok34()  throws InvalidExpressionException {
        String expression = "((2+X)*(3+X))/(2+X) = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("3+X",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok35()  throws InvalidExpressionException {
        String expression = "-(-(2+3)) = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("5",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok36()  throws InvalidExpressionException {
        String expression = "-(-5) = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("5",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok37()  throws InvalidExpressionException {
        String expression = "-(-(2+x)) = 2+X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("2+X",estado.getNewNode().toExpression());
    }

    /*@Test
    public void stepThrough_ok38()  throws InvalidExpressionException {
        String expression = "-------5 = -5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-5",estado.getNewNode().toExpression());
    }*/

    /*@Test
    public void stepThrough_ok39()  throws InvalidExpressionException {
        String expression = "--(-----5) + 6= 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }*/

    @Test
    public void stepThrough_ok40()  throws InvalidExpressionException {
        String expression = "X^2 + 3 - X*X = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("3",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok41()  throws InvalidExpressionException {
        String expression = "-(2*X) * -(2 + 2) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("8X",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok42()  throws InvalidExpressionException {
        String expression = "(X-4)-5 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X-9",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok43()  throws InvalidExpressionException {
        String expression = "5-X-4 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-X+1",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok44()  throws InvalidExpressionException {
        String expression = "(3*X)*(4*X) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("12X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok45()  throws InvalidExpressionException {
        String expression = "(12*X^2)/27 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("4X^2/9",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok46()  throws InvalidExpressionException {
        String expression = "X^2 - 12X^2 + 5X^2 - 7 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-6X^2-7",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok48()  throws InvalidExpressionException {
        String expression = "(3*X)*(4*X) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("12X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok49()  throws InvalidExpressionException {
        String expression = "(3+X)*(4+X)*(X+5) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X^3+12X^2+47X+60",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok50()  throws InvalidExpressionException {
        String expression = "-2X^2 * (3X - 4) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-6X^3+8X^2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok51()  throws InvalidExpressionException {
        String expression = "X^2 - X^2*(12 + 5X) - 7 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-5X^3-11X^2-7",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok52()  throws InvalidExpressionException {
        String expression = "(5+X)*(X+3) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X^2+8X+15",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok53()  throws InvalidExpressionException {
        String expression = "(X-2)*(X-4) = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X^2-6X+8",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok54()  throws InvalidExpressionException {
        String expression = "(X - 2)^2 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X^2-4X+4",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok55()  throws InvalidExpressionException {
        String expression = "(3X + 5)^2 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("9X^2+30X+25",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok56()  throws InvalidExpressionException {
        String expression = "(X + 3 + 4)^2 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("X^2+14X+49",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok57()  throws InvalidExpressionException {
        String expression = "5*X + (1/2)*X = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("11/2X",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok58()  throws InvalidExpressionException {
        String expression = "X + X/2 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("3/2 X",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok59()  throws InvalidExpressionException {
        String expression = "2 + 5/2 + 3 = 15/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("15/2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok69()  throws InvalidExpressionException {
        String expression = "9/18-5/18 = 2/9";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("2/9",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok70()  throws InvalidExpressionException {
        String expression = "2*(X+3)/3 = 2/9";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("2X/3+2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok71()  throws InvalidExpressionException {
        String expression = "(2 / X) * X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok72()  throws InvalidExpressionException {
        String expression = "5/18 - 9/18 = -2/9";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-2/9",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok73()  throws InvalidExpressionException {
        String expression = "9/18 = 1/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("1/2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok74()  throws InvalidExpressionException {
        String expression = "X/(2/3) + 5 = 1/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("3/2 X + 5",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok75()  throws InvalidExpressionException {
        String expression = "(2+X)/6 = 1/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("1/3+X/6",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok76()  throws InvalidExpressionException {
        String expression = "(-x)/(x) = -1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("-1",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok77()  throws InvalidExpressionException {
        String expression = "6 / (2X^2) = -1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("3/(X^2)",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok78()  throws InvalidExpressionException {
        String expression = "2 + R(4) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("4",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok79()  throws InvalidExpressionException {
        String expression = "2 / (3X^2) + 5= 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("2/(3X^2)+5",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok80()  throws InvalidExpressionException {
        String expression = "(2X + 3)^2 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("4X^2+12X+9",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok81()  throws InvalidExpressionException {
        String expression = "1 + 1/2 = 3/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("3/2",estado.getNewNode().toExpression());
    }

    @Test
    public void stepThrough_ok82()  throws InvalidExpressionException {
        String expression = "4X*3*5 = 24/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        List<NodeStatus> listaNodos =  super.stepThrough(node);
        NodeStatus estado = listaNodos.get(listaNodos.size() - 1);
        Assert.assertEquals("60X",estado.getNewNode().toExpression());
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
        Assert.assertEquals("X/3+3/3+X/3", estado.getNewNode().toExpression());
    }

    @Test
    public void breakUpNumeratorSearch_ok4() throws InvalidExpressionException{
        String expression = " (2+X)/4 = (2/4 + X/4)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertEquals("2/4+X/4", estado.getNewNode().toExpression());
    }

    @Test
    public void breakUpNumeratorSearch_ok5() throws InvalidExpressionException{
        String expression = " 2*(X+3)/3 = 2 * (X/3 + 3/3)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.breakUpNumeratorSearch(flattenedNode);
        Assert.assertEquals("2*(X/3+3/3)", estado.getNewNode().toExpression());
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
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
    public void collectAndCombineSearch_ok3() throws InvalidExpressionException{
        String expression = "10^3 * 10^2 = 10^5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertEquals("10^5",estado.getNewNode().toExpression());
    }

    @Test
    public void collectAndCombineSearch_ok4() throws InvalidExpressionException{
        String expression = "2^4 * 2 * 2^4 * 2 = 2^10";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertEquals("2^4 * 2 * 2^4 * 2 = 2^10", "2^10",estado.getNewNode().toExpression());
    }

    @Test
    public void collectAndCombineSearch_ok5() throws InvalidExpressionException{
        String expression = "2X + 4X + X = 7X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertEquals("7X",estado.getNewNode().toExpression());
    }

    @Test
    public void collectAndCombineSearch_ok6() throws InvalidExpressionException{
        String expression = "X * X^2 * X = X^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertEquals("X * X^2 * X = X^4","X^4",estado.getNewNode().toExpression());
    }

    @Test
    public void collectAndCombineSearch_ok7() throws InvalidExpressionException{
        String expression = "3 * R(11) - 2 * R(11)= 1* R(11)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertEquals("3 * R(11) - 2 * R(11)= 1* R(11)", "1*R(11)",estado.getNewNode().toExpression());
    }

    @Test
    public void collectAndCombineSearch_ok8() throws InvalidExpressionException{
        String expression = "3 * R(11) - 2 * R(11)= 1* R(11)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);
        Assert.assertEquals("1*R(11)",estado.getNewNode().toExpression());
    }

    @Test
    public void collectAndCombineSearch_ok9() throws InvalidExpressionException{
        String expression = "10^2 * 10 = 10^3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(10^2)*(10^1)");
        expectedResults.add("10^(2+1)");
        expectedResults.add("10^3");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals("10^2 * 10 = 10^3", expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };

    }

    @Test
    public void collectAndCombineSearch_ok10() throws InvalidExpressionException{
        String expression = "2 * 2^3 = 2^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(2^1)*(2^3)");
        expectedResults.add("2^(1+3)");
        expectedResults.add("2^4");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok11() throws InvalidExpressionException{
        String expression = "3^3 * 3 * 3 = 3^5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(3^3)*(3^1)*(3^1)");
        expectedResults.add("3^(3+1+1)");
        expectedResults.add("3^5");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok12() throws InvalidExpressionException{
        String expression = "R(X) + R(X) = 2 * R(X)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("1*R(X)+1*R(X)");
        expectedResults.add("(1+1)*R(X)");
        expectedResults.add("2*R(X)");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok13() throws InvalidExpressionException{
        String expression = "4*(R(2))^2 + 7*(R(2))^2 + (R(2))^2 = 12 * R(2)^2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("4*(R(2)^2)+7*(R(2)^2)+1*(R(2)^2)");
        expectedResults.add("(4+7+1)*(R(2)^2)");
        expectedResults.add("12*(R(2)^2)");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals("4*R(2)^2 + 7*R(2)^2 + R(2)^2 = 12 * R(2)^2", expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok14() throws InvalidExpressionException{
        String expression = "10*R(5X) - 2*R(5X) = 8 * R(5X)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(10-2)*R(5X)");
        expectedResults.add("8*R(5X)");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals("10*R(5X) - 2*R(5X) = 8 * R(5X)", expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok15() throws InvalidExpressionException{
        String expression = "X+X = 2X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("X+X");
        expectedResults.add("(1+1)*X");
        expectedResults.add("2X");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok16() throws InvalidExpressionException{
        String expression = "4X^2 + 7X^2 + X^2= 12X^2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);


        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("4X^2+7X^2+X^2");
        expectedResults.add("(4+7+1)*X^2");
        expectedResults.add("12X^2");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok17() throws InvalidExpressionException{
        String expression = "X^2*X*X= X^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("X^2*X^1*X^1");
        expectedResults.add("X^(2+1+1)");
        expectedResults.add("X^4");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok18() throws InvalidExpressionException{
        String expression = "X * X^3 = X^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("X^1*X^3");
        expectedResults.add("X^(1+3)");
        expectedResults.add("X^4");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void collectAndCombineSearch_ok19() throws InvalidExpressionException{
        String expression = "2X * X^2 * 5X = X^4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.collectAndCombineSearch(flattenedNode);

        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(2*5)*(X*X^2*X)");
        expectedResults.add("10*(X*X^2*X)");
        expectedResults.add("10X^4");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void distributeSearch_ok1() throws InvalidExpressionException {
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

   /* @Test
    public void distributeSearch_ok2() throws InvalidExpressionException {
        String expression = "-(4*9*x^2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertTrue("-(4*9*x^2) = 4", estado.getChangeType() == NodeStatus.ChangeTypes.DISTRIBUTE_NEGATIVE_ONE);
    }*/

    @Test
    public void distributeSearch_ok3() throws InvalidExpressionException {
        String expression = "-(X+3) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(-X-3)",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok4() throws InvalidExpressionException {
        String expression = "-(X - 3) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(-X+3)",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok5() throws InvalidExpressionException {
        String expression = "-(-X^2 + 3X^6) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(X^2-3X^6)",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok6() throws InvalidExpressionException {
        String expression = "-(X*3) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("-X*3",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok7() throws InvalidExpressionException {
        String expression = "-(-X * 3) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("X*3",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok8() throws InvalidExpressionException {
        String expression = "-(-X^2 * 3X^6) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("X^2*3X^6",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok9() throws InvalidExpressionException {
        String expression = "(5+X)*(X+3)= 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(5*(X+3)+X*(X+3))");
        expectedResults.add("5X+15+X^2+3X");

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void distributeSearch_ok10() throws InvalidExpressionException {
        String expression = "-2X^2 * (3X - 4) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(-2X^2*3X-2X^2*-4)");
        expectedResults.add("-6X^3+8X^2");

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void distributeSearch_ok11() throws InvalidExpressionException {
        String expression = "(3 / X^2 + X / (X^2 + 3)) * (X^2 + 3) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals( "(3 / X^2 + X / (X^2 + 3)) * (X^2 + 3) = 4", "((3*(X^2+3))/X^2)+((X*(X^2+3))/(X^2+3))",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok12() throws InvalidExpressionException {
        String expression = "(3 / X^2 + X / (X^2 + 3)) * (5 / X + X^5) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("((3/X^2)*(5/X)+(3/X^2)*X^5)+((X/(X^2+3))*(5/X)+(X/(X^2+3))*X^5)",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok13() throws InvalidExpressionException {
        String expression = "(2 / X +  3X^2) * (X^3 + 1) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("((2*(X^3+1))/X+3X^2*(X^3+1))");
        expectedResults.add("(2*(X^3+1))/X+3X^5+3X^2");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void distributeSearch_ok14() throws InvalidExpressionException {
        String expression = "(2X + X^2) * (1 / (X^2 -4) + 4X^2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("((1*(2X+X^2))/(X^2-4)+4X^2*(2X+X^2))");
        expectedResults.add("(1*(2X+X^2))/(X^2-4)+8X^3+4X^4");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void distributeSearch_ok15() throws InvalidExpressionException {
        String expression = "(2X + X^2) * (3X^2 / (X^2 -4) + 4X^2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("((3X^2*(2X+X^2))/(X^2-4)+4X^2*(2X+X^2))");
        expectedResults.add("(3X^2*(2X+X^2))/(X^2-4)+8X^3+4X^4");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };
    }

    @Test
    public void distributeSearch_ok16() throws InvalidExpressionException {
        String expression = "(2X + 3)^2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(2X+3)*(2X+3)",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok17() throws InvalidExpressionException {
        String expression = "(X + 3 + 4)^2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(X+3+4)*(X+3+4)",estado.getNewNode().toExpression());
    }

    @Test
    public void distributeSearch_ok18() throws InvalidExpressionException {
        String expression = "X + 2 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("X+2",estado.getNewNode().toExpression());
    }

   /* @Test
    public void distributeSearch_ok19() throws InvalidExpressionException {
        String expression = "(X + 2)^-1 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(X+2)^-1",estado.getNewNode().toExpression());
    }*/
/*
    @Test
    public void distributeSearch_ok20() throws InvalidExpressionException {
        String expression = "(X + 1)^(1/2) = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.distributeSearch(flattenedNode);
        Assert.assertEquals("(X+1)^(1/2)",estado.getNewNode().toExpression());
    }*/

    @Test
    public void divisionSearch_ok1() throws InvalidExpressionException{
        String expression = "4 = 4";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void divisionSearch_ok2() throws InvalidExpressionException{
        String expression = "2/(x/6) = 12";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.MULTIPLY_BY_INVERSE);
    }

    @Test
    public void divisionSearch_ok3() throws InvalidExpressionException{
        String expression = "6/X/5 = 6 / (X * 5)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertEquals("6/(X*5)",estado.getNewNode().toExpression());
    }

    @Test
    public void divisionSearch_ok4() throws InvalidExpressionException{
        String expression = "-(6/X/5) = -(6 / (X * 5))";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertEquals("-(6/(X*5))",estado.getNewNode().toExpression());
    }

    @Test
    public void divisionSearch_ok5() throws InvalidExpressionException{
        String expression = "-6/x/5 = -6 / (X * 5)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertEquals("-6/(X*5)",estado.getNewNode().toExpression());
    }

    @Test
    public void divisionSearch_ok6() throws InvalidExpressionException{
        String expression = "2/X = 2 / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertEquals("2/X",estado.getNewNode().toExpression());
    }

    @Test
    public void divisionSearch_ok7() throws InvalidExpressionException{
        String expression = "X/(2/3) = X * 3/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divisionSearch(flattenedNode);
        Assert.assertEquals("X*(3/2)",estado.getNewNode().toExpression());
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
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
    public void multiplyFractionsSearch_ok3() throws InvalidExpressionException{
        String expression = "3 * 1/5 * 5/9 = (3 * 1 * 5) / (5 * 9)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(3*1*5)/(5*9)",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok4() throws InvalidExpressionException{
        String expression = "3/7 * 10/11 = (3 * 10) / (7 * 11)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(3*10)/(7*11)",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok5() throws InvalidExpressionException{
        String expression = "2 * 5/X = (2 * 5) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(2*5)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok6() throws InvalidExpressionException{
        String expression = "2 * (5/X) = (2 * 5) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(2*5)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok7() throws InvalidExpressionException{
        String expression = "(5/X) * (2/X) = (5 * 2) / (X * X)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(5*2)/(X*X)",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok8() throws InvalidExpressionException{
        String expression = "(5/X) * X = (5X) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(5*X)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok9() throws InvalidExpressionException{
        String expression = "2X * 9/X = (2X * 9) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(2X*9)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok10() throws InvalidExpressionException{
        String expression = "-3/8 * 2/4 = (-3 * 2) / (8 * 4)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(-3*2)/(8*4)",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok11() throws InvalidExpressionException{
        String expression = "(-1/2) * 4/5 = (-1 * 4) / (2 * 5)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(-1*4)/(2*5)",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok12() throws InvalidExpressionException{
        String expression = "4 * (-1/X) = (4 * -1) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(4*-1)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok13() throws InvalidExpressionException{
        String expression = "X * 2X / X = (X * 2X) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(X*2X)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok14() throws InvalidExpressionException{
        String expression = "-(1/2) * (1/2) = (-1 * 1) / (2 * 2)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("((-1)*1)/(2*2)",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok15() throws InvalidExpressionException{
        String expression = "X * -(1/X) = (X * -1) / X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(X*-1)/X",estado.getNewNode().toExpression());
    }

    @Test
    public void multiplyFractionsSearch_ok16() throws InvalidExpressionException{
        String expression = "-(5/X) * -(X/X) = (-5 * -X) / (X * X)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.multiplyFractionsSearch(flattenedNode);
        Assert.assertEquals("(-5*-X)/(X*X)",estado.getNewNode().toExpression());
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
        String expression = "0*X = 0";
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
        Assert.assertEquals("2+X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeAdditionOfZero_ok4() throws InvalidExpressionException{
        String expression = "2+X+0 = 2 + X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeAdditionOfZero(flattenedNode);
        Assert.assertEquals("2+X",estado.getNewNode().toExpression());
    }

    @Test
    public void removeAdditionOfZero_ok5() throws InvalidExpressionException{
        String expression = "0+2+X = 2 + X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeAdditionOfZero(flattenedNode);
        Assert.assertEquals("2+X",estado.getNewNode().toExpression());
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

    /*@Test
    public void removeExponentBaseOne_ok4() throws InvalidExpressionException {
        String expression = "1^(2 + 3 + 5/4 + 7 - 6/7) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.removeExponentBaseOne(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }*/

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
        String expression = "-(-5) = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyDoubleUnaryMinus(flattenedNode);
        Assert.assertEquals("5",estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyDoubleUnaryMinus_ok4() throws InvalidExpressionException{
        String expression = "-(-X) = X";
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
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.REMOVE_EXPONENT_BY_ONE);
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
        Assert.assertEquals("2X*2*-1",estado.getNewNode().toExpression());
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
        String expression = "1*X = X";
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
        Assert.assertEquals("2*X^2",estado.getNewNode().toExpression());
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
        Assert.assertEquals("12X/27",estado.getNewNode().toExpression());
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
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.NO_CHANGE);
    }

    @Test
    public void cancelLikeTerms_ok2() throws InvalidExpressionException {
        String expression = "(2x^2 * 5) / 2x^2 = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
     //   Assert.assertTrue(estado.getChangeType() == NodeStatus.ChangeTypes.CANCEL_TERMS);
        Assert.assertEquals("(2x^2 * 5) / 2x^2 = 5", "5", estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok3() throws InvalidExpressionException {
        String expression = "2/2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok4() throws InvalidExpressionException {
        String expression = "X^2/X^2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    /*@Test
    public void cancelLikeTerms_ok5() throws InvalidExpressionException {
        String expression = "X^3/X^2 = X^(3 - (2))";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("X^(3-(2))",estado.getNewNode().toExpression());
    }*/

    /*@Test
    public void cancelLikeTerms_ok6() throws InvalidExpressionException {
        String expression = "-(7+X)^8/(7+X)^2 = -(7 + X)^(8 - (2))";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("-(7+X)^(8-(2))",estado.getNewNode().toExpression());
    }*/

    @Test
    public void cancelLikeTerms_ok7() throws InvalidExpressionException {
        String expression = "(2X^2 * 5) / (2X^2) = 5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("5",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok8() throws InvalidExpressionException {
        String expression = "2X^2 / (2X^2 * 5) = 1/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("1/5",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok9() throws InvalidExpressionException {
        String expression = "(4X^2) / (5X^2) = (4) / (5)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("4/5",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok10() throws InvalidExpressionException {
        String expression = "(2X+5)^8 / (2X+5)^2 = (2X+5)^8 / (2X+5)^2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("(2X+5)^8/(2X+5)^2",estado.getNewNode().toExpression());
    }

   /* @Test
    public void cancelLikeTerms_ok11() throws InvalidExpressionException {
        String expression = "(4X^3) / (5X^2) = (4X^(3 - (2))) / (5)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("(4X^(3-(2)))/(5)",estado.getNewNode().toExpression());
    }*/

    @Test
    public void cancelLikeTerms_ok12() throws InvalidExpressionException {
        String expression = "-X / -X = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok13() throws InvalidExpressionException {
        String expression = " 2/ (4X) = 1 / (2X)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("1/(2X)",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok14() throws InvalidExpressionException {
        String expression = " 2/ (4X^2) = 1 / (2X^2)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("1/(2X^2)",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok15() throws InvalidExpressionException {
        String expression = " 2 * X / X = 2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("2/1",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok16() throws InvalidExpressionException {
        String expression = " (35 * R(7)) / (5 * R(5)) = (7 * R(7)) / R(5)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("(7*R(7))/R(5)",estado.getNewNode().toExpression());
    }

    @Test
    public void cancelLikeTerms_ok17() throws InvalidExpressionException {
        String expression = " 6/(2X) = 3 / (X)";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.cancelLikeTerms(flattenedNode);
        Assert.assertEquals("3/(X)",estado.getNewNode().toExpression());
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
    public void simplifyPolynomialFraction_ok3() throws InvalidExpressionException {
        String expression = "2X/4 = X/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertEquals("X/2", estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyPolynomialFraction_ok4() throws InvalidExpressionException {
        String expression = "9X/3 = 3X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertEquals("3X", estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyPolynomialFraction_ok5() throws InvalidExpressionException {
        String expression = "X/-3 = -1/3 X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertEquals("-X/3", estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyPolynomialFraction_ok6() throws InvalidExpressionException {
        String expression = "-3X/-2 = 3/2 X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertEquals("3X/2", estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyPolynomialFraction_ok7() throws InvalidExpressionException {
        String expression = "-X/-1 = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertEquals("X", estado.getNewNode().toExpression());
    }

    @Test
    public void simplifyPolynomialFraction_ok8() throws InvalidExpressionException {
        String expression = "12X^2/27 = X";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.simplifyPolynomialFraction(flattenedNode);
        Assert.assertEquals("4X^2/9", estado.getNewNode().toExpression());
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

    @Test
    public void divideByGCD_ok1() throws InvalidExpressionException {
        String expression = "2/4 = 1/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        Assert.assertEquals("1/2",estado.getNewNode().toExpression());
    }

    @Test
    public void divideByGCD_ok2() throws InvalidExpressionException {
        String expression = "9/3 = 3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        Assert.assertEquals("3",estado.getNewNode().toExpression());
    }

    @Test
    public void divideByGCD_ok3() throws InvalidExpressionException {
        String expression = "12/27 = 4/9";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        Assert.assertEquals("4/9",estado.getNewNode().toExpression());
    }

    @Test
    public void divideByGCD_ok4() throws InvalidExpressionException {
        String expression = "1/-3 = -1/3";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        Assert.assertEquals("-1/3",estado.getNewNode().toExpression());
    }

    @Test
    public void divideByGCD_ok5() throws InvalidExpressionException {
        String expression = "-3/-2 = 3/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        Assert.assertEquals("3/2",estado.getNewNode().toExpression());
    }

    @Test
    public void divideByGCD_ok6() throws InvalidExpressionException {
        String expression = "-1/-1 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        Assert.assertEquals("1",estado.getNewNode().toExpression());
    }

    @Test
    public void divideByGCD_ok7() throws InvalidExpressionException {
        String expression = "15/6 = 5/2";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(5*3)/(2*3)");
        expectedResults.add("5/2");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };

    }

    @Test
    public void divideByGCD_ok8() throws InvalidExpressionException {
        String expression = "24/40 = 3/5";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus estado = super.divideByGCD(flattenedNode);
        List<NodeStatus> subpasos = estado.getSubsteps();
        ArrayList<String> expectedResults = new ArrayList<String>();
        expectedResults.add("(3*8)/(5*8)");
        expectedResults.add("3/5");

        Assert.assertEquals(expectedResults.size(), subpasos.size());

        for (int i=0; i <subpasos.size(); i++){
            Assert.assertEquals(expectedResults.get(i),subpasos.get(i).getNewNode().toExpression());
        };

    }

    @Test
    public void canCollectLikeTerms_ok1() throws InvalidExpressionException {
        String expression = "2+2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertFalse(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok2() throws InvalidExpressionException {
        String expression = "X^2+X^2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertFalse(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok3() throws InvalidExpressionException {
        String expression = "X + 2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertFalse(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok4() throws InvalidExpressionException {
        String expression = "(X+2+X) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertTrue(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok5() throws InvalidExpressionException {
        String expression = "X+2+X = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertTrue(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok6() throws InvalidExpressionException {
        String expression = "X^2 + 5 + X + X^2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertTrue(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok7() throws InvalidExpressionException {
        String expression = "R(2) + R(2) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertFalse(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok8() throws InvalidExpressionException {
        String expression = "2*2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertFalse(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok9() throws InvalidExpressionException {
        String expression = "X^2 * 2X^2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertTrue(resultado);
    }

    @Test
    public void canCollectLikeTerms_ok10() throws InvalidExpressionException {
        String expression = "X * 2 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        boolean resultado = super.canCollectLikeTerms(flattenedNode);
        Assert.assertFalse(resultado);
    }

    @Test
    public void collectLikeTerms_ok1() throws InvalidExpressionException {
        String expression = "2+X+7 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("X+(2+7)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok2() throws InvalidExpressionException {
        String expression = "X + 4 + X + 5 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(X+X)+(4+5)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok3() throws InvalidExpressionException {
        String expression = "R(2) + 100 + R(2) = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(R(2)+R(2))+100",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok4() throws InvalidExpressionException {
        String expression = "X^2 + X + X^2 + X = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(X^2+X^2)+(X+X)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok5() throws InvalidExpressionException {
        String expression = "X^2 + 5 + X^2 + 5 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(X^2+X^2)+(5+5)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok6() throws InvalidExpressionException {
        String expression = "2X^2 + X + X^2 + 3X  = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(2X^2+X^2)+(X+3X)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok7() throws InvalidExpressionException {
        String expression = "R(2)^3 + R(2)^3 - 6X  = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(R(2)^3+R(2)^3)-6X",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok8() throws InvalidExpressionException {
        String expression = "4X + 7 * R(11) - X - 2 * R(11)  = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(4X-X)+(7*R(11)-2*R(11))",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok9() throws InvalidExpressionException {
        String expression = "X^2 * 5 * X * 9 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(5*9)*(X^2*X)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok10() throws InvalidExpressionException {
        String expression = "5X^2 * -4X * 9 = 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(5*-4*9)*(X^2*X)",resultado.getNewNode().toExpression());
    }

    @Test
    public void collectLikeTerms_ok11() throws InvalidExpressionException {
        String expression = "5X^2 * -X * 9= 1";
        Tree tree = (new ParserService()).parseExpression(expression);
        TreeNode node = tree.getRootNode().getLeftNode();
        TreeNode flattenedNode = TreeUtils.flattenOperands(node);
        NodeStatus resultado = super.collectLikeTerms(flattenedNode);
        Assert.assertEquals("(5*-1*9)*(X^2*X)",resultado.getNewNode().toExpression());
    }
}
