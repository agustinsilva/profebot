package ar.com.profebot.expression.manager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.com.profebot.service.ExpressionsManager;

public class GetTermAndContextToIAModuleTest {

    @Before
    public void setUp() {
    }

    @Test
    public void detectTermPassage() {
        List<String> result;

        result = ExpressionsManager.getTermAndContextFromReduction("x+1=3", "x+1-1=3-1");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("x+1", result.get(1));
        Assert.assertEquals("x+1-1", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("x+1=3", "x=3-1");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("x+1", result.get(1));
        Assert.assertEquals("3-1", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("3+x>2+2(x+3)+2", "x>2+2(x+3)+2-3");
        Assert.assertEquals(">", result.get(0));
        Assert.assertEquals("2+2(x+3)+2", result.get(1));
        Assert.assertEquals("2+2(x+3)+2-3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("3+x>2-2(x-3)-2", "x>2-2(x-3)-2-3");
        Assert.assertEquals(">", result.get(0));
        Assert.assertEquals("2-2(x-3)-2", result.get(1));
        Assert.assertEquals("2-2(x-3)-2-3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("3+x>2+2(x+3)+2", "x>2+2(x+3)+2-3");
        Assert.assertEquals(">", result.get(0));
        Assert.assertEquals("2+2(x+3)+2", result.get(1));
        Assert.assertEquals("2+2(x+3)+2-3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("x/3=1", "x/3*3=1*3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("x/3", result.get(1));
        Assert.assertEquals("x/3*3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("x/3=1+1+2+3", "x=(1+1+2+3)*3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("1+1+2+3", result.get(1));
        Assert.assertEquals("(1+1+2+3)*3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("x/3=1+1+2+3", "x*3=(1+1+2+3)*3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("1+1+2+3", result.get(1));
        Assert.assertEquals("(1+1+2+3)*3", result.get(2));
    }

    @Test
    public void detectReduction() {
        List<String> result;

        result = ExpressionsManager.getTermAndContextFromReduction("x+1+1=3", "x+2=3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("1+1", result.get(1));
        Assert.assertEquals("x+1+1", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("x+1-1=3", "x+0=3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("1-1", result.get(1));
        Assert.assertEquals("x+1-1", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("3=x+(1+1)*2", "3=x+2+2");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("(1+1)*2", result.get(1));
        Assert.assertEquals("x+(1+1)*2", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("3=x+(1-1)*2", "3=x+0+2");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("(1-1)*2", result.get(1));
        Assert.assertEquals("x+(1-1)*2", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("3=3+3(x-1)-3", "3=3+3x-3-3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("3(x-1)", result.get(1));
        Assert.assertEquals("3+3(x-1)-3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("(x-1)^2=3", "x^2+x+1=3");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("(x-1)^2", result.get(1));
        Assert.assertEquals("(x-1)^2", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("2(x+1)+3=0", "2x+2+3=0");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("2(x+1)", result.get(1));
        Assert.assertEquals("2(x+1)+3", result.get(2));

        result = ExpressionsManager.getTermAndContextFromReduction("2(-x-1)-3=0", "-2x-2-3=0");
        Assert.assertEquals("=", result.get(0));
        Assert.assertEquals("2(-x-1)", result.get(1));
        Assert.assertEquals("2(-x-1)-3", result.get(2));
    }
}