package ar.com.profebot.factoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.service.ExpressionsManager;
import ar.com.profebot.service.FactoringManager;

public class FactoringManagerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void getPolynomialGeneralForm() {
        Map<Integer, Double> terms;

        // 1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        Assert.assertEquals("1", FactoringManager.getPolynomialGeneralForm(terms));

        // -1
        terms = new HashMap<>();
        terms.put(0, -1.0);
        Assert.assertEquals("-1", FactoringManager.getPolynomialGeneralForm(terms));

        // x
        terms = new HashMap<>();
        terms.put(1, 1.0);
        Assert.assertEquals("x", FactoringManager.getPolynomialGeneralForm(terms));

        // -x
        terms = new HashMap<>();
        terms.put(1, -1.0);
        Assert.assertEquals("-x", FactoringManager.getPolynomialGeneralForm(terms));

        // x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 1.0);
        Assert.assertEquals("x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -1.0);
        Assert.assertEquals("-x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // 3x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 3.0);
        Assert.assertEquals("3*x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -3x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -3.0);
        Assert.assertEquals("-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));

       // x^2+3x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 3.0);
        terms.put(2, 1.0);
        Assert.assertEquals("x^2+3*x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -x^2-3x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -3.0);
        terms.put(2, -1.0);
        Assert.assertEquals("-x^2-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // 4x^2+3x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 3.0);
        terms.put(2, 4.0);
        Assert.assertEquals("4*x^2+3*x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -4x^2-3x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -3.0);
        terms.put(2, -4.0);
        Assert.assertEquals("-4*x^2-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // -4x^2-3x-2 (disordered)
        terms = new HashMap<>();
        terms.put(1, -3.0);
        terms.put(2, -4.0);
        terms.put(0, -2.0);
        Assert.assertEquals("-4*x^2-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));
    }
}