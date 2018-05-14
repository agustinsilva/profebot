package ar.com.profebot.container;

import org.junit.Assert;
import org.junit.Test;

import ar.com.profebot.exceptions.InvalidExpressionException;
import ar.com.profebot.services.ParserService;

public class TreeTest {
	
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
		String expression = "(X-1)^2=X^3-5^2";
		Tree tree = (new ParserService()).parseExpression(expression);
		Assert.assertEquals(expression, tree.toExpression());
		Assert.assertEquals("(X-1)^2", tree.getRootNode().getLeftNode().toExpression());
		Assert.assertEquals("X-1", tree.getRootNode().getLeftNode().getLeftNode().toExpression());
		Assert.assertEquals("X^3", tree.getRootNode().getRightNode().getLeftNode().toExpression());
		Assert.assertEquals("5^2", tree.getRootNode().getRightNode().getRightNode().toExpression());
	}
	
	@Test
	public void parse_ok_4()  throws InvalidExpressionException {
		String expression = "R(X-1)-8=X^3-5^2";
		Tree tree = (new ParserService()).parseExpression(expression);
		Assert.assertEquals(expression, tree.toExpression());
		Assert.assertEquals("R(X-1)", tree.getRootNode().getLeftNode().getLeftNode().toExpression());
	}

	@Test
	public void parse_ok_5()  throws InvalidExpressionException {
		String expression = "6X*7=X/(X+5)";
		Tree tree = (new ParserService()).parseExpression(expression);
		Assert.assertEquals(expression, tree.toExpression());
	}
}
