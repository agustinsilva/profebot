package ar.com.profebot.expression.ruffini;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ar.com.profebot.service.RuffiniService;
import flanagan.complex.Complex;
import flanagan.math.Polynomial;

public class RuffiniExpresionsTest {

    @Before
    public void setUp() {
    }

    @Test
    public void FactorizeQuadraticPolynomial() {
        String polinomialString = "x^2+3x+2";
        int[] coeficientes = new int[] {2,3,1};
        Polynomial polinomialPosta = new Polynomial(coeficientes);
        String NextStep = "";
        try {
            NextStep = RuffiniService.FactorizeToNextStep(polinomialPosta);
        }
        catch (Exception ex){

        }
        Assert.assertEquals("(x+2).(x+1)", NextStep);
    }
}
