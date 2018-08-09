package ar.com.profebot.expression.manager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
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


    }
}