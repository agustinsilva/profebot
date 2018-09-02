package ar.com.profebot.expression.manager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ar.com.profebot.service.ExpressionsManager;

public class PhotoExpressionsManager {

    @Before
    public void setUp() {
    }

    @Test
    public void ParseBigFraction() {
        String equation = "\\frac { ( 3 - 1 ) x ^ { 2 } + 5 x + 2 } { ( 4 x + 5 ) } = 10 x + 3";
        ExpressionsManager.setEquationPhoto(equation, null);

        Assert.assertEquals("((3-1)*X^2+5*X+2)/((4*X+5))=10*X+3", ExpressionsManager.getEquationPhoto());
    }

    @Test
    public void ParseFractionInsideFraction() {
        String equation = "\\frac { ( 3 - 1 ) x ^ { 2 } + 5 x + \\frac { 1 } { 2 } } { ( 4 x + 5 ) } = 10 x + 3";
        ExpressionsManager.setEquationPhoto(equation, null);

        Assert.assertEquals("((3-1)*X^2+5*X+(1)/(2))/((4*X+5))=10*X+3", ExpressionsManager.getEquationPhoto());
    }

    @Test
    public void ParseSumOfFraction() {
        String equation = "\\frac { 3 } { 4 } + \\frac { 5 } { 8 } x \\cdot 4 = 3 + 10 ^ { 3 }";
        ExpressionsManager.setEquationPhoto(equation, null);
        Assert.assertEquals("(3)/(4)+(5)/(8)*X*4=3+10^3", ExpressionsManager.getEquationPhoto());
    }

    @Test
    public void ParseRegularFraction() {
        String equation = "\\frac { 3 + 2 ^ { 4 } } { 3 x } = 5";
        ExpressionsManager.setEquationPhoto(equation, null);
        Assert.assertEquals("(3+2^4)/(3*X)=5", ExpressionsManager.getEquationPhoto());
    }

    @Test
    public void ParseFractionWithAnotherFractionInDenominator() {
        String equation = "\\frac { 3 + 2 ^ { 4 } } { \\frac { 3 x } { 5 } } = 5";
        ExpressionsManager.setEquationPhoto(equation, null);
        Assert.assertEquals("(3+2^4)/((3*X)/(5))=5", ExpressionsManager.getEquationPhoto());
    }

    @Test
    public void ParseSquareEquation() {
        String equation = "\\sqrt { ( 5 + 3 x ) } = 3";
        ExpressionsManager.setEquationPhoto(equation, null);
        Assert.assertEquals("R((5+3*X))=3", ExpressionsManager.getEquationPhoto());
    }

}
