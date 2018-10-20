package ar.com.profebot.factoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.activities.SolvePolynomialActivity;
import ar.com.profebot.service.FactoringManager;

public class PolynomialWriterTest {

    private List<Map<Integer, Double>> list;

    @Before
    public void setUp() {
        list = new ArrayList<>();
    }

    @Test
    public void polinomioVacio() {
        Assert.assertEquals("P(x) =", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoPositivo() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 3.0);
        }});
        Assert.assertEquals("P(x) = 3x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoPositivoYExponenteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, 3.0);
        }});
        Assert.assertEquals("P(x) = 3x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoPositivoConExponenteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(0, 3.0);
        }});
        Assert.assertEquals("P(x) = 3", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoPositivoConCoeficienteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 1.0);
        }});
        Assert.assertEquals("P(x) = x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoNegativo() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, -3.0);
        }});
        Assert.assertEquals("P(x) = -3x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoNegativoYExponenteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -3.0);
        }});
        Assert.assertEquals("P(x) = -3x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoNegativoConCoeficienteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, -1.0);
        }});
        Assert.assertEquals("P(x) = -x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConDosTerminosPositivos() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 3.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(2, 1.0);
        }});
        Assert.assertEquals("P(x) = 3x<sup>2</sup>+x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void cuadraticaBasica() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(0, 1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(1, 2.0);
        }});
        Assert.assertEquals("P(x) = x<sup>2</sup>+1+2x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConDosTerminosPositivosYUnoNegativo() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 3.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(3, 1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(4, -1.0);
        }});

        Assert.assertEquals("P(x) = 3x<sup>2</sup>+x<sup>3</sup>-x<sup>4</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConDosTerminosPositivosUnoNegativoYUnoConExponenteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 3.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(3, 1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(4, -1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(0, -1.0);
        }});
        Assert.assertEquals("P(x) = 3x<sup>2</sup>+x<sup>3</sup>-x<sup>4</sup>-1", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConDosTerminosPositivosUnoNegativoUnoConExponenteNuloYUnoConExponenteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 3.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(3, 1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(4, -1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(0, -1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(1, -3.0);
        }});
        Assert.assertEquals("P(x) = 3x<sup>2</sup>+x<sup>3</sup>-x<sup>4</sup>-1-3x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoConCoeficienteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(null, null);
        }});
        Assert.assertEquals("P(x) = <b>?</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoConExponenteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(null, -4.0);
        }});
        Assert.assertEquals("P(x) = <b>-4</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoConExponenteIgualACero() {
        list.add(new HashMap<Integer, Double>(){{
            put(0, -4.0);
        }});
        Assert.assertEquals("P(x) = -4x<sup><b>0</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void polinomioConUnSoloTerminoConExponenteIgualACeroYCoeficienteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(0, 1.0);
        }});
        Assert.assertEquals("P(x) = 1x<sup><b>0</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void polinomioConUnSoloTerminoConExponenteIgualACeroYCoeficienteUnitarioIngresandoCoeficiente() {
        list.add(new HashMap<Integer, Double>(){{
            put(0, 1.0);
        }});
        Assert.assertEquals("P(x) = 1", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void polinomioConUnSoloTerminoConExponenteUnitarioIngresandoExponente() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, 2.0);
        }});
        Assert.assertEquals("P(x) = 2x<sup><b>1</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void polinomioConDosTerminosYConCoeficienteNuloYPositivoDefault() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -4.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(null, null);
        }});

        Assert.assertEquals("P(x) = -4x+<b>?</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, "+", true));
    }

    @Test
    public void polinomioConDosTerminosYConExponenteNuloYPositivoDefault() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -4.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(null, 2.0);
        }});

        Assert.assertEquals("P(x) = -4x<b>+2</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, "+", true));
    }

    @Test
    public void polinomioConDosTerminosYConExponenteNuloYPositivoDefaultIngresandoExponente() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -4.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(null, 2.0);
        }});

        Assert.assertEquals("P(x) = -4x+2x<sup><b>?</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, "+", false));
    }

    @Test
    public void polinomioConDosTerminosYConExponenteNuloYCoeficienteUnitarioYPositivoDefaultIngresandoCoeficiente() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -4.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(null, 1.0);
        }});

        Assert.assertEquals("P(x) = -4x<b>+1</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, "+", true));
    }

    @Test
    public void polinomioConTresTerminosYConExponenteNuloYPositivoDefaultIngresandoExponente() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -4.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(0, -1.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(null, 2.0);
        }});

        Assert.assertEquals("P(x) = -4x-1+2x<sup><b>?</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, "+", false));
    }

    @Test
    public void polinomioConDosTerminosYConCoeficienteNuloYNegativoDefault() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, -4.0);
        }});
        list.add(new HashMap<Integer, Double>(){{
            put(null, null);
        }});

        Assert.assertEquals("P(x) = -4x-<b>?</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, "-", true));
    }

    // Pruebas con coeficientes y exponentes nulos o unitarios

    @Test
    public void coeficienteNuloYExponenteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(null, null);
        }});
        Assert.assertEquals("P(x) = <b>?</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
    }

    @Test
    public void coeficienteUnitarioYExponenteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(null, 1.0);
        }});
        Assert.assertEquals("P(x) = <b>1</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 1x<sup><b>?</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteUnitarioYExponenteIgualA0() {
        list.add(new HashMap<Integer, Double>(){{
            put(0, 1.0);
        }});
        Assert.assertEquals("P(x) = 1", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 1x<sup><b>0</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteUnitarioYExponenteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, 1.0);
        }});
        Assert.assertEquals("P(x) = x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 1x<sup><b>1</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteUnitarioYExponenteNumerico() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 1.0);
        }});
        Assert.assertEquals("P(x) = x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 1x<sup><b>2</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteNumericoYExponenteNulo() {
        list.add(new HashMap<Integer, Double>(){{
            put(null, 3.0);
        }});
        Assert.assertEquals("P(x) = <b>3</b>x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 3x<sup><b>?</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteNumericoYExponenteIgualA0() {
        list.add(new HashMap<Integer, Double>(){{
            put(0, 3.0);
        }});
        Assert.assertEquals("P(x) = 3", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 3x<sup><b>0</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteNumericoYExponenteUnitario() {
        list.add(new HashMap<Integer, Double>(){{
            put(1, 3.0);
        }});
        Assert.assertEquals("P(x) = 3x", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 3x<sup><b>1</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }

    @Test
    public void coeficienteNumericoYExponenteNumerico() {
        list.add(new HashMap<Integer, Double>(){{
            put(2, 3.0);
        }});
        Assert.assertEquals("P(x) = 3x<sup>2</sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, true));
        Assert.assertEquals("P(x) = 3x<sup><b>2</b></sup>", FactoringManager.getCurrentPolynomialEnteredAsText(list, null, false));
    }
}