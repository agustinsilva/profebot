package ar.com.profebot.expression.manager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.service.ExpressionsManager;

public class DetectQuadraticExpressionsTest {

    @Before
    public void setUp() {
    }

    @Test
    public void detectQuadraticTerms() {
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x^2"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x^2+3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3-x^2"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3+x^2+3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3+x^2-3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3+(x/2)^2-3"));

        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x*x"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x*x+3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3+x*x"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3+x*x+3"));

        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(x+1)^2"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("1-(x+1)^2"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x-(x+1)^2+3"));

        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(x+1)*(x-1)"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(x+1)*(x-1)+3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3-(x+1)*(x-1)+3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3-(5-x-1)*(1+x)+3"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(x+1)*(x)"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(x+1)*x"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(x)*(x-1)"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x*(x-1)"));

        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("x*(1+x-1)"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("(1+x-1)*x"));
        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("3x*(1+x-1)"));

        Assert.assertTrue(ExpressionsManager.isQuadraticExpression("1+2+x+2+x-x*3-3(1^3+x+4/5)*(1-3x)+5+x"));
    }
}