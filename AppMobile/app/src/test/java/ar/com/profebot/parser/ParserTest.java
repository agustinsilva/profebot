package ar.com.profebot.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;

public class ParserTest {

    @Before
    public void setUp() {
        System.out.println(" Setting up Parser Service Test...");
    }

    @Test
    public void parse_ok_1()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("X+3=3+X");
        Assert.assertEquals("=", tree.getRootNode().getValue());

        Assert.assertEquals("+", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("3", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("+", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("3", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getRightNode().getRightNode().getValue());
    }

    @Test
    public void parse_ok_2()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("X/5-3=3+4*X");
        Assert.assertEquals("=", tree.getRootNode().getValue());

        Assert.assertEquals("-", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("/", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getLeftNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("5", tree.getRootNode().getLeftNode().getLeftNode().getRightNode().getValue());
        Assert.assertEquals("3", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("+", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("3", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("*", tree.getRootNode().getRightNode().getRightNode().getValue());
        Assert.assertEquals("4", tree.getRootNode().getRightNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getRightNode().getRightNode().getRightNode().getValue());
    }

    @Test
    public void parse_ok_3()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("(X-1)^2=X^3-5^2");
        Assert.assertEquals("=", tree.getRootNode().getValue());

        Assert.assertEquals("^", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("-", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getLeftNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("1", tree.getRootNode().getLeftNode().getLeftNode().getRightNode().getValue());
        Assert.assertEquals("2", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("-", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("X^3", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("^", tree.getRootNode().getRightNode().getRightNode().getValue());
        Assert.assertEquals("5", tree.getRootNode().getRightNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("2", tree.getRootNode().getRightNode().getRightNode().getRightNode().getValue());
    }

    @Test
    public void parse_ok_4()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("R(X-1)-8=X^3-5^2");
        Assert.assertEquals("=", tree.getRootNode().getValue());

        Assert.assertEquals("-", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("R", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("2", tree.getRootNode().getLeftNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("-", tree.getRootNode().getLeftNode().getLeftNode().getRightNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getLeftNode().getLeftNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("1", tree.getRootNode().getLeftNode().getLeftNode().getRightNode().getRightNode().getValue());
        Assert.assertEquals("8", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("-", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("X^3", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("^", tree.getRootNode().getRightNode().getRightNode().getValue());
        Assert.assertEquals("5", tree.getRootNode().getRightNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("2", tree.getRootNode().getRightNode().getRightNode().getRightNode().getValue());
    }

    @Test
    public void parse_ok_5()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("6X*7=X/(X+5)");
        Assert.assertEquals("=", tree.getRootNode().getValue());

        Assert.assertEquals("*", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("6X", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("7", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("/", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("+", tree.getRootNode().getRightNode().getRightNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getRightNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("5", tree.getRootNode().getRightNode().getRightNode().getRightNode().getValue());
    }

    @Test
    public void parse_ok_6()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("X+3>3+X");
        Assert.assertEquals(">", tree.getRootNode().getValue());

        Assert.assertEquals("+", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("3", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("+", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("3", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getRightNode().getRightNode().getValue());
    }

    @Test
    public void parse_ok_7()  throws InvalidExpressionException {
        Tree tree = (new ParserService()).parseExpression("(X-1)^2=X^3-5X^2");
        Assert.assertEquals("=", tree.getRootNode().getValue());

        Assert.assertEquals("^", tree.getRootNode().getLeftNode().getValue());
        Assert.assertEquals("-", tree.getRootNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("X", tree.getRootNode().getLeftNode().getLeftNode().getLeftNode().getValue());
        Assert.assertEquals("1", tree.getRootNode().getLeftNode().getLeftNode().getRightNode().getValue());
        Assert.assertEquals("2", tree.getRootNode().getLeftNode().getRightNode().getValue());

        Assert.assertEquals("-", tree.getRootNode().getRightNode().getValue());
        Assert.assertEquals("X^3", tree.getRootNode().getRightNode().getLeftNode().getValue());
        Assert.assertEquals("5X^2", tree.getRootNode().getRightNode().getRightNode().getValue());

        Assert.assertEquals(new Integer(3), tree.getRootNode().getRightNode().getLeftNode().getExponent());
        Assert.assertEquals(new Integer(1), tree.getRootNode().getRightNode().getLeftNode().getCoefficient());
        Assert.assertEquals(new Integer(2), tree.getRootNode().getRightNode().getRightNode().getExponent());
        Assert.assertEquals(new Integer(5), tree.getRootNode().getRightNode().getRightNode().getCoefficient());
    }
}