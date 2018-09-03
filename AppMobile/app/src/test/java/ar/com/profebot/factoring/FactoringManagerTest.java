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
    public void factorComunSimpleNumericoYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 4x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 4.0);

        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Ninguna raíz
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-0.5));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-0.5), 0.1);

        // Polinomio pendiente: 4(x+1/2)
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(1).intValue());
        Assert.assertEquals(0.5, FactoringManager.polynomialTerms.get(0), 0.1);
        Assert.assertEquals("x+0.5", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
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
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Raíces: 0 y -2
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());

        // Polinomio pendiente: x+2
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(1).intValue());
        Assert.assertEquals(2, FactoringManager.polynomialTerms.get(0).intValue());
        Assert.assertEquals("x+2", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunSimpleYSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+4x
        terms = new HashMap<>();
        terms.put(3, 1.0);
        terms.put(1, 4.0);

        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Raíz 0
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());

        // Polinomio pendiente: x^2+4
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(2).intValue());
        Assert.assertEquals(4, FactoringManager.polynomialTerms.get(0).intValue());
        Assert.assertEquals("x^2+4", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunDobleYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+4x^2
        terms = new HashMap<>();
        terms.put(3, 1.0);
        terms.put(2, 4.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Raíz 0
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-4.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-4.0).intValue());

        // Polinomio pendiente: x+2
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(1).intValue());
        Assert.assertEquals(4, FactoringManager.polynomialTerms.get(0).intValue());
        Assert.assertEquals("x+4", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

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
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raíz: -1 doble
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaCompletaConRaizDoble() {
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
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raíz: -1 doble
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaCompletaConRaicesSimples() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+5x+6
        terms = new HashMap<>();
        terms.put(0, 6.0);
        terms.put(1, 5.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raíces: -2 y -3
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertTrue(FactoringManager.roots.contains(-3.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-3.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaSinTerminoB() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2-1
        terms = new HashMap<>();
        terms.put(0, -1.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raices: 1 y -1
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(1.0));
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(1.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaSinTerminoC() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2-x
        terms = new HashMap<>();
        terms.put(1, -1.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raices: 0 y 1
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 4x^2-1
        terms = new HashMap<>();
        terms.put(0, -1.0);
        terms.put(2, 4.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Raíz: 1/2
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(1.0/2));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(1.0/2).intValue());

        // Plinomio pendiente: 4x+2
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("4*x+2", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertFalse(FactoringManager.end);
    }

    @Test
    public void gaussYNoSePuedeSeguirFactorizandoPorSumasYRestas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(3, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Raiz: -1
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente: x^2-x+1
        Assert.assertEquals(3, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x^2-x+1", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussYSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+3x^2+3x+9
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 3.0);
        terms.put(2, 3.0);
        terms.put(3, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Raiz: -1
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente: x^2+2x+1
        Assert.assertEquals(3, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x^2+2*x+1", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertFalse(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+3x^2+3x+9
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 3.0);
        terms.put(2, 3.0);
        terms.put(3, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Raiz: -1
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente: x^2+2x+1
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x+1", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }


}