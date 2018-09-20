package ar.com.profebot.expression.manager;

import com.profebot.PhotoEcuation.CameraFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.activities.EnterPolinomialActivity;

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

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
    }

    @Test
    public void ParseClassicPolynomialNegativeLinealTerm() {
        String latex = "x ^ { 4 } - x ^ { 3 } + 3 x ^ { 2 } - 5 x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, -5.0);
        myMap.put(2, 3.0);
        myMap.put(3, -1.0);
        myMap.put(4, 1.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
    }

    @Test
    public void ParseClassicPolynomialNegativeLinealTermComplex() {
        String latex = "3 x ^ { 2 } - 5 x - 15 x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, -20.0);
        myMap.put(2, 3.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
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

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
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

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
    }

    @Test
    public void ParseGroupPotentialTermsPolynomial() {
        String latex = "x ^ { 4 } - 3x ^ { 4 } + 3 x ^ { 2 } + x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, 1.0);
        myMap.put(2, 3.0);
        myMap.put(4, -2.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
    }

    @Test
    public void ParseGroupLinealTermsPolynomial() {
        String latex = "x ^ { 3 } + 3 x ^ { 2 } + x + 5x + 1";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 1.0);
        myMap.put(1, 6.0);
        myMap.put(2, 3.0);
        myMap.put(3, 1.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
    }

    @Test
    public void ParseGroupIndependentTermsPolynomial() {
        String latex = "x ^ { 4 } + x + 1 + 9 +20";

        CameraFragment fragment = new CameraFragment();
        fragment.SetPolinomialForPolinomialActivity(latex);

        Map<Integer, Double> myMap = new HashMap<>();
        //Exponente, Coeficiente
        myMap.put(0, 30.0);
        myMap.put(1, 1.0);
        myMap.put(4, 1.0);

        Assert.assertEquals(EnterPolinomialActivity.polynomialTerms, myMap);
    }

}