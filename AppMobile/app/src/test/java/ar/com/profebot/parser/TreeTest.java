package ar.com.profebot.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;

public class TreeTest {

    @Before
    public void setUp() {
        System.out.println(" Setting up Tree Test...");
    }

    @Test
    public void parse_ok_1()  throws InvalidExpressionException {
        String expression = "X+3=3+X";
        Tree tree = (new ParserService()).parseExpression(expression);
        Assert.assertEquals(expression, tree.toExpression());
    }

    @Test
    public void parse_ok_2()  throws InvalidExpressionException {
        String expression = "X/5-3=3+4*X";
        Tree tree = (new ParserService()).parseExpression(expression);
        Assert.assertEquals(expression, tree.toExpression());
    }

    @Test
    public void parse_ok_3()  throws InvalidExpressionException {
        String expression = "(X-1)^2=X^3-(5^2)";
        Tree tree = (new ParserService()).parseExpression(expression);
        Assert.assertEquals(expression, tree.toExpression());
        Assert.assertEquals("(X-1)^2", tree.getRootNode().getLeftNode().toExpression());
        Assert.assertEquals("X-1", tree.getRootNode().getLeftNode().getLeftNode().toExpression());
        Assert.assertEquals("X^3", tree.getRootNode().getRightNode().getLeftNode().toExpression());
        Assert.assertEquals("5^2", tree.getRootNode().getRightNode().getRightNode().toExpression());
    }

    @Test
    public void parse_ok_4()  throws InvalidExpressionException {
        String expression = "R(X-1)-8=X^3-(5^2)";
        Tree tree = (new ParserService()).parseExpression(expression);
        Assert.assertEquals(expression, tree.toExpression());
        Assert.assertEquals("R(X-1)", tree.getRootNode().getLeftNode().getLeftNode().toExpression());
    }

    @Test
    public void parse_ok_5()  throws InvalidExpressionException {
        testExpression("6X*7=X/(X+5)");
    }

    @Test
    public void parse_lineal_1()  throws InvalidExpressionException {
        testExpression("X+1=2");
    }

    @Test
    public void parse_lineal_2()  throws InvalidExpressionException {
        testExpression("X+1=2+3");
    }

    @Test
    public void parse_lineal_3()  throws InvalidExpressionException {
        testExpression("X+1=2+3+4");
    }

    @Test
    public void parse_lineal_4()  throws InvalidExpressionException {
        testExpression("X+2X=3");
    }

    @Test
    public void parse_lineal_5()  throws InvalidExpressionException {
        testExpression("X+2X+3=2+3");
    }

    @Test
    public void parse_lineal_6()  throws InvalidExpressionException {
        testExpression("X+1=X+2");
    }

    @Test
    public void parse_lineal_7()  throws InvalidExpressionException {
        testExpression("X+2-4=4-5");
    }

    @Test
    public void parse_lineal_8()  throws InvalidExpressionException {
        testExpression("X+5-2X=3-4");
    }

    @Test
    public void parse_lineal_9()  throws InvalidExpressionException {
        testExpression("X-3X+4-5=-X-2X+3X+5");
    }

    @Test
    public void parse_lineal_10()  throws InvalidExpressionException {
        testExpression("X-X=2");
    }

    @Test
    public void parse_lineal_11()  throws InvalidExpressionException {
        testExpression("X=X+2");
    }

    @Test
    public void parse_lineal_12()  throws InvalidExpressionException {
        testExpression("X*0=3");
    }

    @Test
    public void parse_lineal_13()  throws InvalidExpressionException {
        testExpression("3*(3+1)=3X");
    }

    @Test
    public void parse_lineal_14()  throws InvalidExpressionException {
        testExpression("3*(3-1)=3X");
    }

    @Test
    public void parse_lineal_15()  throws InvalidExpressionException {
        testExpression("3*(X+1)=3X");
    }

    @Test
    public void parse_lineal_16()  throws InvalidExpressionException {
        testExpression("3*(X+1)=2*(-X+4)");
    }

    @Test
    public void parse_lineal_17()  throws InvalidExpressionException {
        testExpression("(1/3)*(3+4)=X");
    }

    @Test
    public void parse_lineal_18()  throws InvalidExpressionException {
        testExpression("(1/3)*(X+3)=4");
    }

    @Test
    public void parse_lineal_19()  throws InvalidExpressionException {
        testExpression("3*4*5*X+5=3");
    }

    @Test
    public void parse_lineal_20()  throws InvalidExpressionException {
        testExpression("1+2+3+4+5+X=3");
    }

    @Test
    public void parse_cuadratica_1()  throws InvalidExpressionException {
        testExpression("X^2=4");
    }

    @Test
    public void parse_cuadratica_2()  throws InvalidExpressionException {
        testExpression("X^2+2X=4");
    }

    @Test
    public void parse_cuadratica_3()  throws InvalidExpressionException {
        testExpression("X^2+2X+1=0");
    }

    @Test
    public void parse_cuadratica_4()  throws InvalidExpressionException {
        testExpression("(X+1)^2=0");
    }

    @Test
    public void parse_cuadratica_5()  throws InvalidExpressionException {
        testExpression("(X+1)^2-4=0");
    }

    @Test
    public void parse_cuadratica_6()  throws InvalidExpressionException {
        testExpression("(X+1)*(X+2)=0");
    }

    @Test
    public void parse_cuadratica_7()  throws InvalidExpressionException {
        testExpression("(X+1)*(X+2)=4");
    }

    @Test
    public void parse_cuadratica_8()  throws InvalidExpressionException {
        testExpression("1/X=2X+3");
    }

    @Test
    public void parse_cuadratica_9()  throws InvalidExpressionException {
        testExpression("R(X^4)=3");
    }

    @Test
    public void parse_cuadratica_10()  throws InvalidExpressionException {
        testExpression("X*X+4X=4");
    }

    @Test
    public void parse_cuadratica_11()  throws InvalidExpressionException {
        testExpression("X*(X+1)=5");
    }

    @Test
    public void parse_cuadratica_12()  throws InvalidExpressionException {
        testExpression("X*(X+1)=X*(1-X)");
    }

    @Test
    public void parse_cuadratica_13()  throws InvalidExpressionException {
        testExpression("X^2*(1/X+2)=3");
    }

    @Test
    public void parse_exponente_con_fraccion()  throws InvalidExpressionException {
        testExpression("X+(1/2)^2=3");
    }

    @Test
    public void parse_raiz_con_fraccion()  throws InvalidExpressionException {
        testExpression("X+R(1/2)=3");
    }

    @Test
    public void parse_division_con_fraccion()  throws InvalidExpressionException {
        testExpression("X+(1/2)/5=3");
    }

    @Test
    public void parse_division_con_fraccion_2()  throws InvalidExpressionException {
        testExpression("X+8/(1/2)=3");
    }

    @Test
    public void parse_raiz_con_fraccion_exponente_division()  throws InvalidExpressionException {
        testExpression("X+(R(1/2)^2)/8=3");
    }

    @Test
    public void parse_fraccion_sumando()  throws InvalidExpressionException {
        testExpression("X=1/3+4");
    }

    private void testExpression(String expression) throws InvalidExpressionException{
        Tree tree = (new ParserService()).parseExpression(expression);
        Assert.assertEquals(expression, tree.toExpression());
    }

}