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
    public void parseExpressionsToLatex() {
        Assert.assertEquals("", ExpressionsManager.mapToLatexAndReplaceComparator(""));

        Assert.assertEquals("x+1", ExpressionsManager.mapToLatexAndReplaceComparator("x+1"));
        Assert.assertEquals("-x+1", ExpressionsManager.mapToLatexAndReplaceComparator("-x+1"));

        Assert.assertEquals("{x}^{4}+1", ExpressionsManager.mapToLatexAndReplaceComparator("x^4+1"));
        Assert.assertEquals("\\left({x}^{4}+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x^4+1)"));
        Assert.assertEquals("-{x}^{4}+1", ExpressionsManager.mapToLatexAndReplaceComparator("-x^4+1"));
        Assert.assertEquals("\\left(- {x}^{4}+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(-x^4+1)"));

        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left(x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x-1)*(x+1)"));
        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left({x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x-1)*(x^2+2*x+1)"));

        Assert.assertEquals("{x}^{2}+2\\cdot x+1", ExpressionsManager.mapToLatexAndReplaceComparator("x^2+2*x+1"));
        Assert.assertEquals("-{x}^{2}+2\\cdot x+1", ExpressionsManager.mapToLatexAndReplaceComparator("-x^2+2*x+1"));

        Assert.assertEquals("-3\\cdot \\left({x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("-3*(x^2+2*x+1)"));
        Assert.assertEquals("-3\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("-3*(-x^2+2*x+1)"));

        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left(-3\\right)\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x-1)*(-3)*(-x^2+2*x+1)"));
        Assert.assertEquals("\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(x+2\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("((x-1)*3)*(x+2)"));
        Assert.assertEquals("\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(\\left(x+2\\right)\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("((x-1)*3)*((x+2))"));
        Assert.assertEquals("\\left(\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(\\left(x+2\\right)\\right)\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(((x-1)*3)*((x+2)))"));
        Assert.assertEquals("\\left(\\left(\\left(\\left(x-1\\right)\\cdot 3\\right)\\right)\\cdot \\left(\\left(x+2\\right)\\right)\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("((((x-1)*3))*((x+2)))"));
    }

    @Test
    public void parseEquationsToLatex() {
        Assert.assertEquals("x+1=\\left(x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("x+1=+(x+1)"));
        Assert.assertEquals("x+1=-\\left(x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("x+1=-(x+1)"));
        Assert.assertEquals("x+1=x+1", ExpressionsManager.mapToLatexAndReplaceComparator("x+1=x+1"));
        Assert.assertEquals("-x+1=-x+1", ExpressionsManager.mapToLatexAndReplaceComparator("-x+1=-x+1"));

        Assert.assertEquals("{x}^{4}+1={x}^{4}+1", ExpressionsManager.mapToLatexAndReplaceComparator("x^4+1=x^4+1"));
        Assert.assertEquals("\\left({x}^{4}+1\\right) \\lt \\left({x}^{4}+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x^4+1)<(x^4+1)"));
        Assert.assertEquals("-{x}^{4}+1 \\gt -{x}^{4}+1", ExpressionsManager.mapToLatexAndReplaceComparator("-x^4+1>-x^4+1"));
        Assert.assertEquals("\\left(- {x}^{4}+1\\right) \\leqslant \\left(- {x}^{4}+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(-x^4+1)<=(-x^4+1)"));

        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left(x+1\\right) \\geqslant \\left(x-1\\right)\\cdot \\left(x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x-1)*(x+1)>=(x-1)*(x+1)"));
        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left({x}^{2}+2\\cdot x+1\\right)=\\left(x-1\\right)\\cdot \\left({x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x-1)*(x^2+2*x+1)=(x-1)*(x^2+2*x+1)"));

        Assert.assertEquals("-3\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)=-3\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("-3*(-x^2+2*x+1)=-3*(-x^2+2*x+1)"));
        Assert.assertEquals("{x}^{2}+2\\cdot x+1={x}^{2}+2\\cdot x+1", ExpressionsManager.mapToLatexAndReplaceComparator("x^2+2*x+1=x^2+2*x+1"));
        Assert.assertEquals("-{x}^{2}+2\\cdot x+1=-{x}^{2}+2\\cdot x+1", ExpressionsManager.mapToLatexAndReplaceComparator("-x^2+2*x+1=-x^2+2*x+1"));
        Assert.assertEquals("-3\\cdot \\left({x}^{2}+2\\cdot x+1\\right)=-3\\cdot \\left({x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("-3*(x^2+2*x+1)=-3*(x^2+2*x+1)"));
        Assert.assertEquals("\\left(x-1\\right)\\cdot \\left(-3\\right)\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)=\\left(x-1\\right)\\cdot \\left(-3\\right)\\cdot \\left(- {x}^{2}+2\\cdot x+1\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(x-1)*(-3)*(-x^2+2*x+1)=(x-1)*(-3)*(-x^2+2*x+1)"));

        Assert.assertEquals("\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(x+2\\right)=\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(x+2\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("((x-1)*3)*(x+2)=((x-1)*3)*(x+2)"));
        Assert.assertEquals("\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(\\left(x+2\\right)\\right)=\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(\\left(x+2\\right)\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("((x-1)*3)*((x+2))=((x-1)*3)*((x+2))"));
        Assert.assertEquals("\\left(\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(\\left(x+2\\right)\\right)\\right)=\\left(\\left(\\left(x-1\\right)\\cdot 3\\right)\\cdot \\left(\\left(x+2\\right)\\right)\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("(((x-1)*3)*((x+2)))=(((x-1)*3)*((x+2)))"));
        Assert.assertEquals("\\left(\\left(\\left(\\left(x-1\\right)\\cdot 3\\right)\\right)\\cdot \\left(\\left(x+2\\right)\\right)\\right)=\\left(\\left(\\left(\\left(x-1\\right)\\cdot 3\\right)\\right)\\cdot \\left(\\left(x+2\\right)\\right)\\right)", ExpressionsManager.mapToLatexAndReplaceComparator("((((x-1)*3))*((x+2)))=((((x-1)*3))*((x+2)))"));
    }
}