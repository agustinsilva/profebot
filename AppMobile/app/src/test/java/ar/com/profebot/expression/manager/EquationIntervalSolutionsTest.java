package ar.com.profebot.expression.manager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import ar.com.profebot.resolutor.service.ResolutorService;
import ar.com.profebot.service.EquationManager;
import ar.com.profebot.service.ExpressionsManager;

public class EquationIntervalSolutionsTest {

    @Before
    public void setUp() {
    }

    @Test
    public void inecuacionesLineales() {
        Assert.assertEquals("(1, +∞)", EquationManager.getIntervalFrom("x>1"));
        Assert.assertEquals("[1, +∞)", EquationManager.getIntervalFrom("x>=1"));
        Assert.assertEquals("(-∞, 1)", EquationManager.getIntervalFrom("x<1"));
        Assert.assertEquals("(-∞, 1]", EquationManager.getIntervalFrom("x<=1"));
    }

    @Test
    public void inecuacionCuadraticaDeParabolaPositivaSinRaices() {
        Assert.assertEquals("(-∞, ∞)", EquationManager.getIntervalFrom("x^2+x>-1"));
        Assert.assertEquals("(-∞, ∞)", EquationManager.getIntervalFrom("x^2+x>=-1"));
        Assert.assertEquals("", EquationManager.getIntervalFrom("x^2+x<-1"));
        Assert.assertEquals("", EquationManager.getIntervalFrom("x^2+x<=-1"));
    }

    @Test
    public void inecuacionCuadraticaDeParabolaNegativaSinRaices() {
        Assert.assertEquals("", EquationManager.getIntervalFrom("x^2+x>1"));
        Assert.assertEquals("", EquationManager.getIntervalFrom("x^2+x>=1"));
        Assert.assertEquals("(-∞, ∞)", EquationManager.getIntervalFrom("x^2+x<1"));
        Assert.assertEquals("(-∞, ∞)", EquationManager.getIntervalFrom("x^2+x<=1"));
    }
}