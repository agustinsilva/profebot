package ar.com.profebot.factoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.SolvePolynomialActivity;
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

    @Test
    public void factorComunSimpleYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+2x
        terms = new HashMap<>();
        terms.put(2, 1.0);
        terms.put(1, 2.0);

        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por factor común
        FactoringManager.factorizeBy(1);

        // Raíz 0
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());

        // Polinomio pendiente: x+2
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(1).intValue());
        Assert.assertEquals(2, FactoringManager.polynomialTerms.get(0).intValue());

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunSimpleYSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 2x^3+4x
        terms = new HashMap<>();
        terms.put(3, 2.0);
        terms.put(1, 4.0);

        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por factor común
        FactoringManager.factorizeBy(1);

        // Raíz 0
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());

        // Polinomio pendiente: 2x^2+4
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(2, FactoringManager.polynomialTerms.get(2).intValue());
        Assert.assertEquals(4, FactoringManager.polynomialTerms.get(0).intValue());

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunDobleYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+2x^2
        terms = new HashMap<>();
        terms.put(3, 1.0);
        terms.put(2, 2.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por factor común
        FactoringManager.factorizeBy(1);

        // Raíz 0
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(0.0).intValue());

        // Polinomio pendiente: x+2
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(1).intValue());
        Assert.assertEquals(2, FactoringManager.polynomialTerms.get(0).intValue());

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaCompleta() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+2x+1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 2.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(2);

        // Raíz: -1 doble
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }
}