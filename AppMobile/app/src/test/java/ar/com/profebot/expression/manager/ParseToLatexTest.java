package ar.com.profebot.expression.manager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.service.ExpressionsManager;

public class ParseToLatexTest {

    @Before
    public void setUp() {
    }

    @Test
    public void parseExpressionToLatex() {
        Assert.assertEquals("x+1", ExpressionsManager.parseToLatex("x+1"));
        Assert.assertEquals("-x+1", ExpressionsManager.parseToLatex("-x+1"));

        Assert.assertEquals("{x}^{4}+1", ExpressionsManager.parseToLatex("x^4+1"));
        Assert.assertEquals("({x}^{4}+1)", ExpressionsManager.parseToLatex("(x^4+1)"));
        Assert.assertEquals("-{x}^{4}+1", ExpressionsManager.parseToLatex("-x^4+1"));
        Assert.assertEquals("(-{x}^{4}+1)", ExpressionsManager.parseToLatex("(-x^4+1)"));

        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left(x+1\\right)", ExpressionsManager.parseToLatex("(x-1)*(x+1)"));
        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left({x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.parseToLatex("(x-1)*(x^2+2*x+1)"));

        Assert.assertEquals("{x}^{2}+2\\cdot x+1", ExpressionsManager.parseToLatex("x^2+2*x+1"));
        Assert.assertEquals("-{x}^{2}+2\\cdot x+1", ExpressionsManager.parseToLatex("-x^2+2*x+1"));

        Assert.assertEquals("-3\\cdot \\left({x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.parseToLatex("-3*(x^2+2*x+1)"));
        Assert.assertEquals("-3\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.parseToLatex("-3*(-x^2+2*x+1)"));

        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left(-3\\right)\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.parseToLatex("(x-1)*(-3)*(-x^2+2*x+1)"));
    }
}