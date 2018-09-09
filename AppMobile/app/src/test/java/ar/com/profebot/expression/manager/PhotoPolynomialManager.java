package ar.com.profebot.expression.manager;

import com.profebot.PhotoEcuation.CameraFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.activities.EnterPolinomialActivity;
import ar.com.profebot.service.ExpressionsManager;

public class PhotoPolynomialManager {

    @Before
    public void setUp() {
    }

    @Test
    public void ParseClassicPolynomial() {
        String latex = "x ^ { 4 } - x ^ { 3 } + 3 x ^ { 2 } + x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, 1.0);
        myMap.put(2, 3.0);
        myMap.put(3, -1.0);
        myMap.put(4, 1.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms,myMap);
    }

    @Test
    public void ParseClassicNegativePolynomial() {
        String latex = "-x ^ { 4 } - x ^ { 3 } + 3 x ^ { 2 } + x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, 1.0);
        myMap.put(2, 3.0);
        myMap.put(3, -1.0);
        myMap.put(4, -1.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms,myMap);
    }

    @Test
    public void ParseFractionPolynomial() {
        String latex = "(3/2)x ^ { 4 } - x ^ { 3 } + 3 x ^ { 2 } + x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, 1.0);
        myMap.put(2, 3.0);
        myMap.put(3, -1.0);
        myMap.put(4, 1.5);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms,myMap);
    }

    @Test
    public void ParseFractionNegativePolynomial() {
        String latex = "-(8/7)x ^ { 4 } - x ^ { 3 } + 3 x ^ { 2 } + x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, 1.0);
        myMap.put(2, 3.0);
        myMap.put(3, -1.0);
        myMap.put(4, -1.14);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms,myMap);
    }
}